package org.example.token;

import lombok.Value;

@Value
public class KeywordToken implements Token {

    TokenType type;
    Position position;


    @Override
    public <T> T getValue() {
        return null;
    }
}
