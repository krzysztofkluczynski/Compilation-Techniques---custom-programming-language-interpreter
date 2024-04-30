package org.example.parser;

import org.example.lexer.Lexer;
import org.example.lexer.error.*;
import org.example.token.Token;

import java.io.IOException;

public class ParserImpl implements Parser{
    Lexer lexer;
    Token token;

    ParserImpl(Lexer lexer) {
        this.lexer = lexer;
    }

    private void nextToken() throws ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IntMaxValueExceededException, IOException, IdentifierTooLongException, DecimalMaxValueExceededException {
        token = lexer.next();
    }


    /**
     * PROGRAM = { FUNCTION_DEFINITION ";"};
     */
    public Program parseProgram() {
        nextToken();
    }

}
