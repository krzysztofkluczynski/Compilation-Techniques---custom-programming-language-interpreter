package org.example.interpreter.error;

import org.example.parser.Structure.OtherComponents.Argument;

import java.util.List;

public class InvalidFunctionCallInterpretingException extends InterpretingException {
    public InvalidFunctionCallInterpretingException(List<Argument> params, List<Object> arguments) {
        super("Invalid function call. Expected: " + params + ", but got: " + arguments);
    }
}
