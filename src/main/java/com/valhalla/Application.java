package com.valhalla;

import java.lang.invoke.MethodHandles;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.runtime.Micronaut;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Application {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private static JDA client;

	public static void main(String[] args) throws LoginException, InterruptedException {
		client = JDABuilder.createLight(System.getenv()
				.get("GREETER_TOKEN"))
			.enableCache(CacheFlag.VOICE_STATE)
			.enableIntents(GatewayIntent.GUILD_VOICE_STATES)
			.build();
		client.awaitReady();
		Micronaut.run(Application.class, args);
		LOG.info("Application ready.");
	}

	public static JDA getDiscordClient() {
		return client;
	}
}
