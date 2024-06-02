package org.example.interpreter.error;

import org.example.parser.Enum.RelativeType;

public class InvalidLogicStatementInterpretingException extends InterpretingException {
    public InvalidLogicStatementInterpretingException(Object left, RelativeType conditionOperand, Object right) {
        super("Invalid logic statement: " + left + " " + conditionOperand.getSymbol() + " " + right);
    }
}
