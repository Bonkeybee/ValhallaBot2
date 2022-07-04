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
		Micronaut.run(Application.class, args);
		client = JDABuilder.createLight("OTM1MDQ5NzMwNTM3NDMxMTYw.GYdgym.jbGO-a7W2aauiP1jSphCNvh17fKMU8yIXwoZuU")
			.enableCache(CacheFlag.VOICE_STATE)
			.enableIntents(GatewayIntent.GUILD_VOICE_STATES)
			.build()
			.awaitReady();
		LOG.info("Application ready.");
	}
}
