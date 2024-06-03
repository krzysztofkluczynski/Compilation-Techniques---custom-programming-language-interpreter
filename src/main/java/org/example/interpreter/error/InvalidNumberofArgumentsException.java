package org.example.interpreter.error;

public class InvalidNumberofArgumentsException extends InterpretingException {
    public InvalidNumberofArgumentsException(String name, int i, int len) {
        super("Invalid number of arguments for function " + name + ". Expected " + i + " but got " + len + ".");
    }
}
