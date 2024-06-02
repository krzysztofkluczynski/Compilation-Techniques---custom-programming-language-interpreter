package org.example.interpreter.error;

import org.example.interpreter.error.InterpretingException;

public class NoValueReturnedFromFunctionInterpretingException extends InterpretingException {
    public NoValueReturnedFromFunctionInterpretingException(String name) {
        super("No value returned from function: " + name);
    }
}
