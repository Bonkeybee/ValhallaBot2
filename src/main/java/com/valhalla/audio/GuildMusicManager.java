package com.valhalla.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
	public final AudioPlayer audioPlayer;
	public final TrackScheduler scheduler;

	private final AudioPlayerSendHandler sendHandler;

	public GuildMusicManager(final AudioPlayerManager audioPlayerManager) {
		this.audioPlayer = audioPlayerManager.createPlayer();
		this.scheduler = new TrackScheduler(this.audioPlayer);
		this.audioPlayer.addListener(this.scheduler);
		this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
	}

	public AudioPlayerSendHandler getSendHandler() {
		return sendHandler;
	}
}
