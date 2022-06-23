package com.valhalla.clients;

import com.valhalla.utils.FileUtils;
import com.valhalla.utils.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.server.exceptions.InternalServerException;
import jakarta.inject.Singleton;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.DescribeVoicesRequest;
import software.amazon.awssdk.services.polly.model.DescribeVoicesResponse;
import software.amazon.awssdk.services.polly.model.Engine;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.TextType;
import software.amazon.awssdk.services.polly.model.Voice;

@Singleton
public class AwsPollyClient {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private static final String ACTOR = "Takumi";
	private static final String STORAGE_DIRECTORY = "synthesized";
	private static final String OPEN_SPEAK = "<speak>";
	private static final String CLOSE_SPEAK = "</speak>";
	private static final int TTL = 300;
	private final Map<String, Date> files = new HashMap<>();
	private PollyClient client;
	private Voice voice;

	public synchronized String synthesize(final String message) {
		final UUID uuid = UUID.randomUUID();
		final String url = STORAGE_DIRECTORY + StringUtils.SLASH + uuid + StringUtils.PERIOD + OutputFormat.MP3;
		final InputStream inputStream = synthesize(getClient(), OPEN_SPEAK + message + CLOSE_SPEAK, getVoice());
		writeToFile(inputStream, url);
		files.put(uuid.toString(), Date.from(Instant.now()));
		return url;
	}

	private synchronized PollyClient getClient() {
		if (client == null) {
			client = PollyClient.builder()
				.region(Region.US_EAST_1)
				.build();
		}
		cleanupStorage();
		return client;
	}

	private synchronized void cleanupStorage() {
		try {
			Files.createDirectories(Paths.get(STORAGE_DIRECTORY));
		} catch (IOException e) {
			throw new InternalServerException("Failed to create directories.", e);
		}
		try (final Stream<Path> paths = Files.walk(Paths.get(STORAGE_DIRECTORY))) {
			paths.filter(Files::isRegularFile)
				.forEach(path -> {
					if (OutputFormat.MP3.toString()
						.equals(FileUtils.getFileExtension(path.getFileName()
							.toString()))) {
						final Date date = files.get(FileUtils.getFileNameWithoutExtension(path.getFileName()
							.toString()));
						if (date == null || Instant.now()
							.minusSeconds(TTL)
							.isAfter(date.toInstant())) {
							try {
								Files.delete(path);
							} catch (IOException e) {
								throw new InternalServerException("Failed to delete file.", e);
							}
						}
					}
				});
		} catch (IOException e) {
			throw new InternalServerException("Failed to read storage directory paths.", e);
		}
	}

	private synchronized Voice getVoice() {
		if (voice == null) {
			final DescribeVoicesRequest describeVoicesRequest = DescribeVoicesRequest.builder()
				.engine(Engine.NEURAL)
				.build();
			final DescribeVoicesResponse describeVoicesResponse = getClient().describeVoices(describeVoicesRequest);
			voice = describeVoicesResponse.voices()
				.stream()
				.filter(x -> x.name()
					.equals(ACTOR))
				.findFirst()
				.orElse(describeVoicesResponse.voices()
					.get(0));
		}
		return voice;
	}

	private static InputStream synthesize(final PollyClient polly, final String message, final Voice voice) {
		final SynthesizeSpeechRequest synthesizeSpeechRequest = SynthesizeSpeechRequest.builder()
			.voiceId(voice.id())
			.engine(Engine.NEURAL)
			.textType(TextType.SSML)
			.text(message)
			.outputFormat(OutputFormat.MP3)
			.build();
		return polly.synthesizeSpeech(synthesizeSpeechRequest);
	}

	private void writeToFile(final InputStream inputStream, final String url) {
		try {
			Files.createDirectories(Paths.get(STORAGE_DIRECTORY));
		} catch (IOException e) {
			throw new InternalServerException("Failed to create directories.", e);
		}
		try (final FileOutputStream outputStream = new FileOutputStream(url, false)) {
			int read;
			final byte[] bytes = new byte[8192];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			inputStream.close();
		} catch (IOException e) {
			throw new InternalServerException("Failed to write stream to file.", e);
		}
	}
}
