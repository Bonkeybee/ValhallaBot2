package com.valhalla.listeners;

import com.valhalla.clients.AwsPollyClient;
import com.valhalla.clients.DiscordClient;
import com.valhalla.configurations.DiscordConfiguration;
import com.valhalla.services.StateService;

import java.util.Objects;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GreeterListener extends ListenerAdapter {

	private final DiscordConfiguration discordConfiguration;
	private final StateService stateService;
	private final DiscordClient discordClient;
	private final AwsPollyClient awsPollyClient;

	public GreeterListener(final DiscordConfiguration discordConfiguration, final StateService stateService, final DiscordClient discordClient, final AwsPollyClient awsPollyClient) {
		this.discordConfiguration = discordConfiguration;
		this.stateService = stateService;
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
		if (!stateService.isDiscordClientReady()) {
			return;
		}
		if (Objects.requireNonNull(event.getChannelJoined())
			.getId()
			.equals(discordConfiguration.generalVoiceId)) {
			String name = event.getMember()
				.getEffectiveName()
				.trim();
			name = name.split("[ /]")[0];
			discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize("Wassup, " + name));
		}
	}

	@Override
	public void onGuildVoiceMove(final GuildVoiceMoveEvent event) {
		if (event.getMember()
			.getUser()
			.isBot()) {
			return;
		}
		if (!stateService.isDiscordClientReady()) {
			return;
		}
		if (Objects.requireNonNull(event.getChannelJoined())
			.getId()
			.equals(discordConfiguration.generalVoiceId) && !Objects.requireNonNull(event.getChannelLeft())
			.getId()
			.equals(discordConfiguration.generalVoiceId)) {
			String name = event.getMember()
				.getEffectiveName()
				.trim();
			name = name.split("[ /]")[0];
			discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize("Wassup, " + name));
		} else if (Objects.requireNonNull(event.getChannelLeft())
			.getId()
			.equals(discordConfiguration.generalVoiceId) && !event.getChannelJoined()
			.getId()
			.equals(discordConfiguration.generalVoiceId)) {
			String name = event.getMember()
				.getEffectiveName()
				.trim();
			name = name.split("[ /]")[0];
			discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize("Cya later, " + name));
		}
	}

	@Override
	public void onGuildVoiceLeave(final GuildVoiceLeaveEvent event) {
		if (event.getMember()
			.getUser()
			.isBot()) {
			return;
		}
		if (!stateService.isDiscordClientReady()) {
			return;
		}
		if (Objects.requireNonNull(event.getChannelLeft())
			.getId()
			.equals(discordConfiguration.generalVoiceId)) {
			String name = event.getMember()
				.getEffectiveName()
				.trim();
			name = name.split("[ /]")[0];
			discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize("Cya later, " + name));
		}
	}
}
