package com.valhalla;

import com.valhalla.clients.DiscordClient;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.server.EmbeddedServer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DataLoader implements ApplicationEventListener<StartupEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	@Inject
	private DiscordClient discordClient;

	public DataLoader(final EmbeddedServer embeddedServer) {
		LOG.info("Listening on {}://{}:{} (isServer={}, isKeepAlive={})", embeddedServer.getScheme(), embeddedServer.getHost(), embeddedServer.getPort(), embeddedServer.isServer(),
			embeddedServer.isKeepAlive());
	}

	@Override
	public void onApplicationEvent(final StartupEvent event) {
		discordClient.getClient();
	}
}
