package com.lootfilters.lang;

import lombok.Getter;

@Getter
public class ParseException extends RuntimeException {
    private final Token token;

    public ParseException(String message, Token token) {
        super(message + " (token=" + token + ")");
        this.token = token;
    }

    public ParseException(String message) {
        super(message);
        this.token = null;
    }
}
