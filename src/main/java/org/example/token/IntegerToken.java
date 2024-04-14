package org.example.token;

import lombok.Value;

@Value
public class IntegerToken implements Token {

    Position position;
    Integer value;

    public TokenType getType() {
        return TokenType.INT_LITERAL;
    }
}
