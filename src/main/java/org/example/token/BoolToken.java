package org.example.token;

import lombok.Value;

@Value
public class BoolToken implements Token {
    Position position;
    Boolean value;

    public TokenType getType() {
        return TokenType.BOOL_LITERAL;
    }
}
