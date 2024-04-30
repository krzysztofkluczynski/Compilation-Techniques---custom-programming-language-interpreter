package org.example.token;

import lombok.Value;

@Value
public class KeywordOrOperandToken implements Token {

    TokenType type;
    Position position;
    String value;

}
