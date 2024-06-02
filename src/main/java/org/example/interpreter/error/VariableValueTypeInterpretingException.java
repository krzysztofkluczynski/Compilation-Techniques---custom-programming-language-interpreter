package org.example.interpreter.error;

import org.example.interpreter.model.Variable;

public class VariableValueTypeInterpretingException extends InterpretingException {

    public VariableValueTypeInterpretingException(Variable variable) {
        super("Can not assign value: " + variable.getValue() +
                " to variable: " + variable.getVariableType().getName() + " " + variable.getName());
    }

}


