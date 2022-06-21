package com.valhalla.configurations;

import javax.validation.constraints.NotNull;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

@ConfigurationProperties("discord")
public class DiscordConfiguration {

	@NotNull
	private String token;

	@NonNull
	public String getToken() {
		return token;
	}

	public void setToken(@NonNull final String token) {
		this.token = token;
	}
}
