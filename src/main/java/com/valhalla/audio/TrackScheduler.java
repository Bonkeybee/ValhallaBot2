package com.valhalla.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackScheduler extends AudioEventAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;

	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

	public void queue(AudioTrack track) {
		if (!this.player.startTrack(track, true) && !this.queue.offer(track)) {
			LOG.error("Queueing track {} failed.", track.getIdentifier());
		}
	}

	public void nextTrack() {
		this.player.startTrack(this.queue.poll(), false);
	}
}
