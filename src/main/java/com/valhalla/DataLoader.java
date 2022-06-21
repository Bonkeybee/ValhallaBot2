package com.valhalla;

import com.valhalla.clients.DiscordClient;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DataLoader implements ApplicationEventListener<StartupEvent> {

	@Inject
	private DiscordClient discordClient;

	@Override
	public void onApplicationEvent(final StartupEvent event) {
		discordClient.getClient();
	}
}
