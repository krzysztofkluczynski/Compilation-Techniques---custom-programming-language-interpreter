package org.example.lexer.error;

import org.example.token.Position;

public class IdentifierTooLongException extends Exception {
    public IdentifierTooLongException(Position position, int maxLength) {
        super("ERROR in" + position.toString() + " | Identifier exceeds maximum length with a length of " + maxLength);
    }
}
