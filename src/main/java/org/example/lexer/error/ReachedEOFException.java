package org.example.lexer.error;

public class ReachedEOFException  extends Exception {
    public ReachedEOFException(String msg) {
        super(msg);
    }
}
