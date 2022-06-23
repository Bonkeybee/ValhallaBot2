package com.valhalla.clients;

import com.valhalla.audio.AudioPlayerSendHandler;
import com.valhalla.audio.PlayerManager;
import com.valhalla.configurations.DiscordConfiguration;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.server.exceptions.InternalServerException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@Singleton
public class DiscordClient {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private JDA client;
	private AudioPlayerSendHandler audioPlayerSendHandler;
	private AudioManager audioManager;

	@Inject
	private DiscordConfiguration discordConfiguration;

	@Inject
	private PlayerManager audioPlayerManager;

	public synchronized JDA getClient() {
		if (client == null) {
			try {
				LOG.info("Discord Bot initializing...");
				client = JDABuilder.createDefault(discordConfiguration.getGreeterToken())
					.enableCache(CacheFlag.VOICE_STATE)
					.enableIntents(GatewayIntent.GUILD_VOICE_STATES)
					.build()
					.awaitReady();
				LOG.info("Discord Bot ready.");
			} catch (final LoginException e) {
				LOG.error("LoginException!", e);
				throw new InternalServerException("Failed to start Discord Bot.", e);
			} catch (final InterruptedException e) {
				LOG.error("InterruptedException", e);
				Thread.currentThread()
					.interrupt();
				throw new InternalServerException("Interrupted exception.", e);
			}
		}
		getAudioManager();
		return client;
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
			final Guild guild = client.getGuildById(discordConfiguration.getGuildId());
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
