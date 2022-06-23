package com.valhalla.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class InputStreamAudioSourceManager implements AudioSourceManager {
	@Override
	public String getSourceName() {
		return "inputstream";
	}

	@Override
	public AudioItem loadItem(final AudioPlayerManager manager, final AudioReference reference) {
		return null;
	}

	@Override
	public boolean isTrackEncodable(final AudioTrack track) {
		return false;
	}

	@Override
	public void encodeTrack(final AudioTrack track, final DataOutput output) throws IOException {

	}

	@Override
	public AudioTrack decodeTrack(final AudioTrackInfo trackInfo, final DataInput input) throws IOException {
		return null;
	}

	@Override
	public void shutdown() {

	}
}
