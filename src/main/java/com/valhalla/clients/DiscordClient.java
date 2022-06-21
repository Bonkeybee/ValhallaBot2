package com.valhalla.clients;

import com.valhalla.configurations.DiscordConfiguration;

import javax.security.auth.login.LoginException;

import io.micronaut.http.server.exceptions.InternalServerException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

@Singleton
public class DiscordClient {

	private JDA client;

	@Inject
	private DiscordConfiguration discord;

	public synchronized JDA getClient() {
		if (client == null) {
			final JDABuilder builder = JDABuilder.createDefault(discord.getToken());
			try {
				client = builder.build();
			} catch (final LoginException e) {
				throw new InternalServerException("Failed to start Discord bot.", e);
			}
		}
		return client;
	}
}
