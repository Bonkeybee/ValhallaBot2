package com.valhalla.utils;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ApplicationUtils {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private ApplicationUtils() {}

	public static void sleep(final long millis) {
		try {
			LOG.debug("Sleep for {} seconds", millis / 1000);
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
			Thread.currentThread()
				.interrupt();
		}
	}
}
