package com.valhalla.listeners;

import com.valhalla.clients.AwsPollyClient;
import com.valhalla.clients.DiscordClient;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;

@Singleton
public class GreeterListener implements EventListener {

	@Inject
	private DiscordClient discordClient;

	@Inject
	private AwsPollyClient awsPollyClient;

	@Override
	public void onEvent(final GenericEvent event) {
		if (event instanceof final GuildVoiceJoinEvent guildVoiceJoinEvent) {
			if (!guildVoiceJoinEvent.getMember()
				.getUser()
				.isBot()) {
				if (guildVoiceJoinEvent.getChannelJoined()
					.getId()
					.equals(System.getenv()
						.get("GENERAL_VOICE_ID"))) {
					String name = guildVoiceJoinEvent.getMember()
						.getEffectiveName()
						.trim();
					name = name.split("[ /]")[0];
					discordClient.play(System.getenv()
						.get("GENERAL_VOICE_ID"), awsPollyClient.synthesize("Wassup, " + name));
				}
			}
		}
		if (event instanceof final GuildVoiceLeaveEvent guildVoiceLeaveEvent) {
			if (!guildVoiceLeaveEvent.getMember()
				.getUser()
				.isBot()) {
				if (guildVoiceLeaveEvent.getChannelLeft()
					.getId()
					.equals(System.getenv()
						.get("GENERAL_VOICE_ID"))) {
					String name = guildVoiceLeaveEvent.getMember()
						.getEffectiveName()
						.trim();
					name = name.split("[ /]")[0];
					discordClient.play(System.getenv()
						.get("GENERAL_VOICE_ID"), awsPollyClient.synthesize("Cya later, " + name));
				}
			}
		}
		if (event instanceof final GuildVoiceMoveEvent guildVoiceMoveEvent) {
			if (!guildVoiceMoveEvent.getMember()
				.getUser()
				.isBot()) {
				if (guildVoiceMoveEvent.getChannelJoined()
					.getId()
					.equals(System.getenv()
						.get("GENERAL_VOICE_ID")) && !guildVoiceMoveEvent.getChannelLeft()
					.getId()
					.equals(System.getenv("GENERAL_VOICE_ID"))) {
					String name = guildVoiceMoveEvent.getMember()
						.getEffectiveName()
						.trim();
					name = name.split("[ /]")[0];
					discordClient.play(System.getenv()
						.get("GENERAL_VOICE_ID"), awsPollyClient.synthesize("Wassup, " + name));
				} else if (guildVoiceMoveEvent.getChannelLeft()
					.getId()
					.equals(System.getenv()
						.get("GENERAL_VOICE_ID")) && !guildVoiceMoveEvent.getChannelJoined()
					.getId()
					.equals(System.getenv()
						.get("GENERAL_VOICE_ID"))) {
					String name = guildVoiceMoveEvent.getMember()
						.getEffectiveName()
						.trim();
					name = name.split("[ /]")[0];
					discordClient.play(System.getenv()
						.get("GENERAL_VOICE_ID"), awsPollyClient.synthesize("Cya later, " + name));
				}
			}
		}
	}
}
