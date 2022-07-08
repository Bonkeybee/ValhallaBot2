package com.valhalla.listeners;

import com.valhalla.clients.DiscordClient;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

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

	@Inject
	private GreeterListener greeterListener;

	public DataLoader(final EmbeddedServer embeddedServer) {
		LOG.info("Listening on {}://{}:{} (isServer={}, isKeepAlive={})", embeddedServer.getScheme(), embeddedServer.getHost(), embeddedServer.getPort(), embeddedServer.isServer(),
			embeddedServer.isKeepAlive());
	}

	@Override
	public void onApplicationEvent(final StartupEvent event) {
		LOG.info("Initializing startup...");
		discordClient.play(System.getenv()
			.get("GENERAL_VOICE_ID"), Objects.requireNonNull(this.getClass()
				.getClassLoader()
				.getResource("sounds/startup.flac"))
			.getFile());
		LOG.info("Adding DiscordClient listeners...");
		discordClient.getClient()
			.addEventListener(greeterListener);
		LOG.info("Startup complete!");
	}
}
