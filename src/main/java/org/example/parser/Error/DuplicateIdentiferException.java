package org.example.parser.Error;

import org.example.token.Position;

public class DuplicateIdentiferException extends Exception{
    public DuplicateIdentiferException(Position position, String name) {
        super("Parsing ERROR In: " + position.toString() + " | Duplicate identifer name: " + name);
    }
}
