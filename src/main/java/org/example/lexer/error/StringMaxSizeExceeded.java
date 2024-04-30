package org.example.lexer.error;

import org.example.token.Position;

public class StringMaxSizeExceeded extends Exception {
    public StringMaxSizeExceeded(Position position, int maxLength) {
        super("ERROR in" + position.toString() + " | String max length excceed, allowed max range is " + maxLength);
    }
}
