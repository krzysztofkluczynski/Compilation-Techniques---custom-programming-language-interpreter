package org.example.interpreter.error;

public class NoMainFunctionInterpretingException extends InterpretingException{
    public NoMainFunctionInterpretingException() {
        super("There is no main function");
    }

}
