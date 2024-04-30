package org.example.lexer;

import org.example.lexer.error.*;
import org.example.token.Token;

import java.io.IOException;

public interface Lexer {
    Token next() throws ReachedEOFException, StringMaxSizeExceeded, IOException, UnkownTokenException, IdentifierTooLongException, IntMaxValueExceededException, DecimalMaxValueExceededException;
}
