package com.github.ledoyen.automocker.examples.property;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class TextService {

	@Value("${text.file}")
	private Resource textFile;

	public String getText() {
		try {
			return IOUtils.toString(textFile.getInputStream(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalStateException("Text file cannot be read", e);
		}
	}
}
