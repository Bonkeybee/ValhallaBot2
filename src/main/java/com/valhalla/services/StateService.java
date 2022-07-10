package com.valhalla.services;

import jakarta.inject.Singleton;

@Singleton
public class StateService {

	private boolean discordClientReady;

	public boolean isDiscordClientWaiting() {
		return !discordClientReady;
	}

	public void setDiscordClientReady(final boolean discordClientReady) {
		this.discordClientReady = discordClientReady;
	}
}
