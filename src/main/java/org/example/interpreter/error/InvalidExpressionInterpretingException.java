package org.example.interpreter.error;

import org.example.interpreter.model.Variable;
import org.example.parser.Enum.MultiplicativeType;

public class InvalidExpressionInterpretingException extends InterpretingException {
    public InvalidExpressionInterpretingException(Variable left, Variable right) {
        super("Invalid multiplicative expression with variables:: " + left + " " + ", " + right);
    }
}
