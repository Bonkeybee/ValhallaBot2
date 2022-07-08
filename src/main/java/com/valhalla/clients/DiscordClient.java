package com.valhalla.clients;

import com.valhalla.Application;
import com.valhalla.audio.AudioPlayerSendHandler;
import com.valhalla.audio.PlayerManager;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.server.exceptions.InternalServerException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

@Singleton
public class DiscordClient {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private AudioPlayerSendHandler audioPlayerSendHandler;
	private AudioManager audioManager;

	@Inject
	private PlayerManager audioPlayerManager;

	public synchronized JDA getClient() {
		return Application.getDiscordClient();
	}

	public void play(final String channelId, final String url) {
		final VoiceChannel channel = getClient().getVoiceChannelById(channelId);
		if (channel != null && url != null) {
			if (!getAudioManager().isConnected() || !channel.getId()
				.equals(Objects.requireNonNull(getAudioManager().getConnectedChannel())
					.getId())) {
				getAudioManager().openAudioConnection(channel);
			}
			LOG.info("Queueing audio {} in {}[{}].", url, channel.getName(), channelId);
			audioPlayerManager.queue(url);
		}
	}

	private synchronized AudioManager getAudioManager() {
		if (audioPlayerSendHandler == null) {
			LOG.info("Discord Bot Audio Player initializing...");
			audioPlayerSendHandler = new AudioPlayerSendHandler(audioPlayerManager.getGuildMusicManager().audioPlayer);
		}
		if (audioManager == null) {
			final Guild guild = getClient().getGuildById(System.getenv()
				.get("GUILD_ID"));
			if (guild != null) {
				audioManager = guild.getAudioManager();
				audioManager.setSendingHandler(audioPlayerSendHandler);
				LOG.info("Discord Bot Audio Player ready.");
			} else {
				throw new InternalServerException("Failed to start Discord Bot Audio Player");
			}
		}
		return audioManager;
	}
}
