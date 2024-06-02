package org.example.interpreter.error;

import org.example.interpreter.error.InterpretingException;
import org.example.interpreter.model.FunctionCallContext;
import org.example.parser.Enum.Type;

public class IncorrectReturnedValueTypeInterpretingException extends InterpretingException {
    public IncorrectReturnedValueTypeInterpretingException(FunctionCallContext functionCallContext, Type returnedType) {
        super("Incorrect returned value type from function: " + functionCallContext.getName() + ". Expected: " + functionCallContext.getFunctionType() + ", but got: " + returnedType);
    }
}
