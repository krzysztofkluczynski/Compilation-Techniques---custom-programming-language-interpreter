package org.example.lexer;

import org.example.lexer.error.*;
import org.example.token.Token;

import java.io.IOException;

public interface Lexer {
    Token next() throws NumberMaxValueExceededException, ReachedEOFException, StringMaxSizeExceeded, IOException, UnkownTokenException, IdentifierTooLongException;
}
