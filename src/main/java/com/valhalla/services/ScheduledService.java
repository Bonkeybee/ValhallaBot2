package com.valhalla.services;

import com.valhalla.clients.DiscordClient;

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

	@Inject
	private StateService stateService;

	@Inject
	private DiscordClient discordClient;

	@Scheduled(initialDelay = "60s")
	public void startup() {
		discordClient.play(System.getenv()
			.get("GENERAL_VOICE_ID"), Objects.requireNonNull(this.getClass()
				.getClassLoader()
				.getResource("sounds/startup.flac"))
			.getFile());
		stateService.setDiscordClientReady(true);
	}
}
