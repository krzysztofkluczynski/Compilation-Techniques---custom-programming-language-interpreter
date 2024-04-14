package org.example.token;

import lombok.Value;

@Value
public class StringToken implements Token {
    Position position;
    String value;

    public TokenType getType() {
        return TokenType.STRING_LITERAL;
    }

}
