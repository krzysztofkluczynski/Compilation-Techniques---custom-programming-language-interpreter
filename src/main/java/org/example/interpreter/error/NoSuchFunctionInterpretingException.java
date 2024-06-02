package org.example.interpreter.error;

import org.example.parser.Structure.Statement.FunctionCall;

public class NoSuchFunctionInterpretingException extends InterpretingException {

    public NoSuchFunctionInterpretingException(FunctionCall functionCall) {
        super("Function named " + functionCall.getName() + " does not exist");
    }
}
