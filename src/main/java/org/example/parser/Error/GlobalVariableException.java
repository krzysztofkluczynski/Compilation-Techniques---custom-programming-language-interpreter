package org.example.parser.Error;

import org.example.token.Position;
import org.example.token.TokenType;

public class GlobalVariableException extends Exception{
    public GlobalVariableException(Position position, TokenType type) {
        super("Parsing ERROR In: " + position.toString() + " | Exepected function definition, got token: " + type.toString());
    }
}
