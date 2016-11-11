package com.github.ledoyen.automocker.extension.mvc;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import com.github.ledoyen.automocker.tools.ThrowingFunction;
import com.google.common.primitives.Ints;

public final class HttpStatusMatcher {

	private HttpStatusMatcher() {
	}

	public static ResultMatcher parse(String status) {
		HttpStatus httpStatus = Optional.of(status)
				.map(ThrowingFunction.silent(HttpStatus::valueOf, e -> null))
				.orElseGet(() -> Optional.of(status)
						.map(Ints::tryParse)
						.map(ThrowingFunction.silent(HttpStatus::valueOf, e -> null))
						.orElse(null));
		if (httpStatus == null) {
			throw new IllegalArgumentException("Unable to parse status [" + status + "]");
		}

		return new ResultMatcher() {
			@Override
			public void match(MvcResult result) throws Exception {
				assertEquals("Response status", new HttpStatusWrapper(httpStatus),
						new HttpStatusWrapper(HttpStatus.valueOf(result.getResponse()
								.getStatus()),
								result.getResponse()
										.getContentAsString()));
			}
		};
	}

	private static final class HttpStatusWrapper {

		private final HttpStatus status;
		private final Object optionalMessage;

		private HttpStatusWrapper(HttpStatus status) {
			this(status, null);
		}

		private HttpStatusWrapper(HttpStatus status, Object optionalMessage) {
			this.status = status;
			this.optionalMessage = optionalMessage;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof HttpStatusWrapper && status.equals(((HttpStatusWrapper) obj).status);
		}

		@Override
		public String toString() {
			return status.value() + " (" + status.getReasonPhrase() + ")"
					+ (optionalMessage != null ? (": " + optionalMessage) : "");
		}
	}
}
