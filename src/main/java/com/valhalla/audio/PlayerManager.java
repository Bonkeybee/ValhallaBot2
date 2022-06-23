package com.valhalla.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import jakarta.inject.Singleton;

@Singleton
public class PlayerManager {
	private final AudioPlayerManager audioPlayerManager;
	private final GuildMusicManager guildMusicManager;

	public PlayerManager() {
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
		AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
		guildMusicManager = new GuildMusicManager(audioPlayerManager);
	}

	public GuildMusicManager getGuildMusicManager() {
		return this.guildMusicManager;
	}

	public void queue(final String url) {
		this.audioPlayerManager.loadItemOrdered(guildMusicManager, url, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(final AudioTrack track) {
				guildMusicManager.scheduler.queue(track);
			}

			@Override
			public void playlistLoaded(final AudioPlaylist playlist) {
				// TODO document why this method is empty
			}

			@Override
			public void noMatches() {
				// TODO document why this method is empty
			}

			@Override
			public void loadFailed(final FriendlyException exception) {
				// TODO document why this method is empty
			}
		});
	}
}
