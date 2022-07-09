package com.valhalla.resources;

import com.valhalla.clients.AwsPollyClient;
import com.valhalla.clients.DiscordClient;
import com.valhalla.configurations.DiscordConfiguration;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;

@Validated
@Controller
public class SpeechResource {

	@Inject
	private DiscordConfiguration discordConfiguration;

	@Inject
	private DiscordClient discordClient;

	@Inject
	private AwsPollyClient awsPollyClient;

	@Post(uri = "/speak", consumes = MediaType.TEXT_PLAIN)
	public MutableHttpResponse<Object> speak(@Body final String message) {
		discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize(message));
		return HttpResponse.ok();
	}
}
