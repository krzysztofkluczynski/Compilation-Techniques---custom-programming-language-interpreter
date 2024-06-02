package org.example.interpreter.error;

import org.example.interpreter.model.Variable;

public class LocalVariableRepeatedInterpretingException extends InterpretingException {

    public LocalVariableRepeatedInterpretingException(Variable variable) {
        super("Variable of name: " + variable.getName() + " is already present in this context");
    }

}

