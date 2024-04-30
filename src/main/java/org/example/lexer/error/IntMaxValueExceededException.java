package org.example.lexer.error;

import org.example.token.Position;

public class IntMaxValueExceededException extends Exception {

        public IntMaxValueExceededException(Position position, int maxLength) {
            super("ERROR in" + position.toString() + " | Decimal part exceeds maximum value being: " + maxLength);
        }

}
