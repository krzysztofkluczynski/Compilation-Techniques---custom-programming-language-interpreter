package org.example.parser.Error;

import org.example.token.Position;
import org.example.token.TokenType;

import java.util.List;

public class ParsingException extends Exception{
    public ParsingException(Position position, List<TokenType> expected, List<TokenType> got ) {
        super("Parsing ERROR in" + position.toString() + " | EXPECTED tokens: " + expected.toString() + ", GOT: " + got.toString());
    }


    public ParsingException(Position position, TokenType expected, TokenType got) {
        super("Parsing ERROR in" + position.toString() + " | EXPECTED tokens: " + expected.toString() + ", GOT: " + got.toString());
    }

    public ParsingException(Position position, List<TokenType> expected, TokenType got) {
        super("Parsing ERROR in" + position.toString() + " | EXPECTED tokens: " + expected.toString() + ", GOT: " + got.toString());
    }



}
