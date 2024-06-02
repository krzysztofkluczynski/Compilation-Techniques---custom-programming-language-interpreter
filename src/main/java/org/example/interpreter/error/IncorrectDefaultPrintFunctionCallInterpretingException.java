package org.example.interpreter.error;

import java.util.List;

public class IncorrectDefaultPrintFunctionCallInterpretingException extends InterpretingException {
    public IncorrectDefaultPrintFunctionCallInterpretingException(List<Object> parsedArguments) {
        super("Incorrect default print function call, expected one argument \n GOT: " + parsedArguments);
    }
}
