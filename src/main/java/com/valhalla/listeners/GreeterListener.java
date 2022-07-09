package com.valhalla.listeners;

import com.valhalla.clients.AwsPollyClient;
import com.valhalla.clients.DiscordClient;
import com.valhalla.configurations.DiscordConfiguration;
import com.valhalla.services.StateService;
import com.valhalla.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GreeterListener extends ListenerAdapter {
	private static final String ALLOWED_CHARACTERS_REGEX = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
	private static final String SPLIT_REGEX = "[ /]";
	private static final String FORMALITY = "-san";
	private static final List<String> GREETINGS = Arrays.asList("Oy", "Hola", "Bonjour", "Howdy", "Hi-ya", "Heya", "Sup", "Wazzup", "What's up", "Yo", "Hi", "Hello", "Hey", "Wassup", "Ohayo",
		"Ohayou", "Konnichiwa");
	private static final List<String> FAREWELLS = Arrays.asList("Laters", "Later", "Take care", "Goodbye", "Bai bai", "Bye bye", "Bye", "See ya", "See ya later", "Adios", "Sayounara",
		"Sayonara");

	private static final Random random = new Random();

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
			discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize(getGreeting() + StringUtils.COMMA + StringUtils.SPACE + getSimpleName(event.getMember())));
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
			discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize(getGreeting() + StringUtils.COMMA + StringUtils.SPACE + getSimpleName(event.getMember())));
		} else if (Objects.requireNonNull(event.getChannelLeft())
			.getId()
			.equals(discordConfiguration.generalVoiceId) && !event.getChannelJoined()
			.getId()
			.equals(discordConfiguration.generalVoiceId)) {
			discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize(getFarewell() + StringUtils.COMMA + StringUtils.SPACE + getSimpleName(event.getMember())));
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
			discordClient.play(discordConfiguration.generalVoiceId, awsPollyClient.synthesize(getFarewell() + StringUtils.COMMA + StringUtils.SPACE + getSimpleName(event.getMember())));
		}
	}

	private String getGreeting() {
		return GREETINGS.get(random.nextInt(GREETINGS.size()));
	}

	private String getFarewell() {
		return FAREWELLS.get(random.nextInt(FAREWELLS.size()));
	}

	private String getSimpleName(final Member member) {
		String name = member.getEffectiveName()
			.trim()
			.replaceAll(ALLOWED_CHARACTERS_REGEX, StringUtils.EMPTY)
			.split(SPLIT_REGEX)[0];
		return name.substring(0, Math.min(name.length(), 18)) + FORMALITY;
	}
}
