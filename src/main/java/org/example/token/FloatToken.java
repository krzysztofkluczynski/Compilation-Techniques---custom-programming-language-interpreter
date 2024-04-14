package org.example.token;

import lombok.Value;

@Value
public class FloatToken implements Token {
    Position position;
    Float value;

    public TokenType getType() {
        return TokenType.FLOAT_LITERAL;
    }

}
