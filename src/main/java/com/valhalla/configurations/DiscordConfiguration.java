package com.valhalla.configurations;

import javax.validation.constraints.NotNull;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

@ConfigurationProperties("discord")
public class DiscordConfiguration {

	@NotNull
	private String guildId;

	@NotNull
	private String generalChannelId;

	@NotNull
	private String greeterToken;

	@NonNull
	public String getGreeterToken() {
		return greeterToken;
	}

	public void setGreeterToken(@NonNull final String greeterToken) {
		this.greeterToken = greeterToken;
	}

	@NonNull
	public String getGuildId() {
		return guildId;
	}

	public void setGuildId(@NonNull final String guildId) {
		this.guildId = guildId;
	}

	@NonNull
	public String getGeneralChannelId() {
		return generalChannelId;
	}

	public void setGeneralChannelId(@NonNull final String generalChannelId) {
		this.generalChannelId = generalChannelId;
	}
}
