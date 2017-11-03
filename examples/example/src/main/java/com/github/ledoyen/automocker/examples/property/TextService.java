package com.github.ledoyen.automocker.examples.property;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class TextService {

    private final String literalText;
    private final Optional<String> optionalText;

    TextService(@Value("${text.literal}")
                        String literalText, @Value("${text.optional:}")
                        Optional<String> optionalText) {
        this.literalText = literalText;
        this.optionalText = optionalText;
    }


    public String getLiteralText() {
        return literalText;
    }

    public String getOptionalText() {
        return optionalText.get();
    }
}
