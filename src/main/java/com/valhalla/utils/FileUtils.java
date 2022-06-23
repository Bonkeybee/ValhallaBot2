package com.valhalla.utils;

import java.util.Optional;

public final class FileUtils {

	public static String getFileExtension(final String fileName) {
		return Optional.ofNullable(fileName)
			.filter(string -> string.contains("."))
			.map(string -> string.substring(fileName.lastIndexOf(".") + 1))
			.orElse(null);
	}

	public static String getFileNameWithoutExtension(final String fileName) {
		return fileName.replaceFirst("[.][^.]+$", "");
	}
}
