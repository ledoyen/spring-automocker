package com.github.ledoyen.automocker.tools;

public final class Classes {

	private Classes() {
	}

	public static boolean isPresent(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
