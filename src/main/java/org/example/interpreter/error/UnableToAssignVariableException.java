package org.example.interpreter.error;

import org.example.interpreter.model.Variable;
import org.example.parser.Enum.Type;

public class UnableToAssignVariableException extends InterpretingException {
    public UnableToAssignVariableException(Variable variable, Type mainType) {
        super("Unable to assign variable " + variable + " to type " + mainType + ".");
    }
}
