package org.example.interpreter.error;

import org.example.interpreter.error.InterpretingException;

public class NoSuchVariableInterpretingException extends InterpretingException {

    public NoSuchVariableInterpretingException(String variableName) {
        super("Variable named " + variableName + " does not exist");
    }

}
