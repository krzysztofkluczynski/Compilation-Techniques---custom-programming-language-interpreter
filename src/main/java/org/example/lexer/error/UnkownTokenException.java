package org.example.lexer.error;

import org.example.token.Position;

public class UnkownTokenException extends Exception {
    public UnkownTokenException(Position position) {
        super("ERROR in" + position.toString() + " | Unknown Token");
    }
}
