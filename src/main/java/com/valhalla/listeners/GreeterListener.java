package com.valhalla.listeners;

import com.valhalla.clients.AwsPollyClient;
import com.valhalla.clients.DiscordClient;

import java.util.Objects;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GreeterListener extends ListenerAdapter {

	private final DiscordClient discordClient;
	private final AwsPollyClient awsPollyClient;

	public GreeterListener(final DiscordClient discordClient, final AwsPollyClient awsPollyClient) {
		this.discordClient = discordClient;
		this.awsPollyClient = awsPollyClient;
	}

	@Override
	public void onGuildVoiceJoin(final GuildVoiceJoinEvent event) {
		if (event.getMember()
			.getUser()
			.isBot()) {
			return;
		}
		if (Objects.requireNonNull(event.getChannelJoined())
			.getId()
			.equals(getVoiceChannelId())) {
			String name = event.getMember()
				.getEffectiveName()
				.trim();
			name = name.split("[ /]")[0];
			discordClient.play(getVoiceChannelId(), awsPollyClient.synthesize("Wassup, " + name));
		}
	}

	@Override
	public void onGuildVoiceMove(final GuildVoiceMoveEvent event) {
		if (event.getMember()
			.getUser()
			.isBot()) {
			return;
		}
		if (Objects.requireNonNull(event.getChannelJoined())
			.getId()
			.equals(getVoiceChannelId()) && !Objects.requireNonNull(event.getChannelLeft())
			.getId()
			.equals(getVoiceChannelId())) {
			String name = event.getMember()
				.getEffectiveName()
				.trim();
			name = name.split("[ /]")[0];
			discordClient.play(getVoiceChannelId(), awsPollyClient.synthesize("Wassup, " + name));
		} else if (Objects.requireNonNull(event.getChannelLeft())
			.getId()
			.equals(getVoiceChannelId()) && !event.getChannelJoined()
			.getId()
			.equals(getVoiceChannelId())) {
			String name = event.getMember()
				.getEffectiveName()
				.trim();
			name = name.split("[ /]")[0];
			discordClient.play(getVoiceChannelId(), awsPollyClient.synthesize("Cya later, " + name));
		}
	}

	@Override
	public void onGuildVoiceLeave(final GuildVoiceLeaveEvent event) {
		if (event.getMember()
			.getUser()
			.isBot()) {
			return;
		}
		if (Objects.requireNonNull(event.getChannelLeft())
			.getId()
			.equals(getVoiceChannelId())) {
			String name = event.getMember()
				.getEffectiveName()
				.trim();
			name = name.split("[ /]")[0];
			discordClient.play(getVoiceChannelId(), awsPollyClient.synthesize("Cya later, " + name));
		}
	}

	private String getVoiceChannelId() {
		return System.getenv()
			.get("GENERAL_VOICE_ID");
	}
}
