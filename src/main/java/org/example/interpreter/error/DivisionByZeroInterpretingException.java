package org.example.interpreter.error;

public class DivisionByZeroInterpretingException extends InterpretingException {
    public DivisionByZeroInterpretingException() {
        super("Division by zero is not allowed!");
    }
}
