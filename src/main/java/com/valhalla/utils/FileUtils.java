package com.valhalla.utils;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	private FileUtils() {}

	public static String getFileExtension(final String fileName) {
		return Optional.ofNullable(fileName)
			.filter(string -> string.contains("."))
			.map(string -> string.substring(fileName.lastIndexOf(".") + 1))
			.orElse(null);
	}

	public static String getFileNameWithoutExtension(final String fileName) {
		return fileName.replaceFirst("[.][^.]+$", "");
	}

	public static String getProjectRoot() {
		final File currentDirFile = new File(StringUtils.PERIOD);
		final String helper = currentDirFile.getAbsolutePath();
		return helper.substring(0, helper.length() - 1);
	}
}
