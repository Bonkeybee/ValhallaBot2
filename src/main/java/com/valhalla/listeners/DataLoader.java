package com.valhalla.listeners;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.server.EmbeddedServer;
import jakarta.inject.Singleton;

@Singleton
public class DataLoader implements ApplicationEventListener<StartupEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	//	@Inject
	//	private DiscordClient discordClient;
	//
	//	@Inject
	//	private GreeterListener greeterListener;
	//
	//	@Inject
	//	private DiscordConfiguration discordConfiguration;

	public DataLoader(final EmbeddedServer embeddedServer) {
		LOG.info("Listening on {}://{}:{} (isServer={}, isKeepAlive={})", embeddedServer.getScheme(), embeddedServer.getHost(), embeddedServer.getPort(), embeddedServer.isServer(),
			embeddedServer.isKeepAlive());
	}

	@Override
	public void onApplicationEvent(final StartupEvent event) {
		//		LOG.info("Initializing startup...");
		//		discordClient.play(discordConfiguration.getGeneralChannelId(), Objects.requireNonNull(this.getClass()
		//				.getClassLoader()
		//				.getResource("sounds/startup.flac"))
		//			.getFile());
		//		LOG.info("Adding DiscordClient listeners...");
		//		discordClient.getClient()
		//			.addEventListener(greeterListener);
		//		LOG.info("Startup complete!");
	}
}
