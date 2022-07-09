package com.valhalla.services;

import com.valhalla.clients.DiscordClient;
import com.valhalla.configurations.DiscordConfiguration;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ScheduledService {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private static final String STARTUP_THEME_PATH = "sounds/startup.flac";

	@Inject
	private DiscordConfiguration configuration;

	@Inject
	private StateService stateService;

	@Inject
	private DiscordClient discordClient;

	@Scheduled(initialDelay = "120s")
	public void startup() {
		discordClient.play(configuration.generalVoiceId, Objects.requireNonNull(this.getClass()
				.getClassLoader()
				.getResource(STARTUP_THEME_PATH))
			.getFile());
		stateService.setDiscordClientReady(true);
	}
}
