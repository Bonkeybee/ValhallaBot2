package com.valhalla.configurations;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class DiscordConfiguration {

	@Value("${GREETER_ID}")
	public String greeterId;

	@Value("${GREETER_TOKEN}")
	public String greeterToken;

	@Value("${GENERAL_VOICE_ID}")
	public String generalVoiceId;

	@Value("${GUILD_ID}")
	public String guildId;
}
