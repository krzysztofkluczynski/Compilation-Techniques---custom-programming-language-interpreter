package org.example.interpreter.error;

import org.example.parser.Enum.Type;

public class InvalidTypeForMethodCallInterpretingException extends InterpretingException {
    public InvalidTypeForMethodCallInterpretingException(Type collectionType) {
        super("Invalid type for method call, got: " + collectionType);
    }
}
