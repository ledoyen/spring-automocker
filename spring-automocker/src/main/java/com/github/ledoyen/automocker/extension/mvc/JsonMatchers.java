package com.github.ledoyen.automocker.extension.mvc;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import net.minidev.json.JSONArray;

public final class JsonMatchers {

	private JsonMatchers() {
	}

	public static Matcher<Object> is(String expected) {
		return new BaseMatcher<Object>() {

			@Override
			public boolean matches(Object item) {
				if (item instanceof JSONArray && ((JSONArray) item).size() == 1) {
					return ((JSONArray) item).get(0)
							.equals(expected);
				} else {
					return expected.equals(item);
				}
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("<is ")
						.appendValue(expected)
						.appendText(">");
			}
		};
	}
}
