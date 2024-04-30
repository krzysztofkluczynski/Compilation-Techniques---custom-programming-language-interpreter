package org.example.lexer.error;

import org.example.token.Position;

public class DecimalMaxValueExceededException extends Exception {
    public DecimalMaxValueExceededException(Position position, int maxLength) {
        super("ERROR in" + position.toString() + " | Decimal part exceeds maximum length being: " + maxLength);
    }
}
