package com.valhalla.services;

import com.valhalla.clients.DiscordClient;
import com.valhalla.configurations.DiscordConfiguration;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ScheduledService {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private static final String STARTUP_THEME_PATH = "https://drive.google.com/uc?export=download&id=1p2hcU0J5iRHRMmaO37-KNyiC5VNJ5NKK";

	@Inject
	private DiscordConfiguration configuration;

	@Inject
	private StateService stateService;

	@Inject
	private DiscordClient discordClient;

	@Scheduled(initialDelay = "0s")
	public void startup() {
		discordClient.play(configuration.generalVoiceId, STARTUP_THEME_PATH);
		stateService.setDiscordClientReady(true);
	}
}
