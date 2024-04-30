package org.example.lexer.error;

import org.example.token.Position;

public class ReachedEOFException  extends Exception {
    public ReachedEOFException(Position position) {
        super("ERROR in" + position.toString() + " | Reached end of file");
    }
}
