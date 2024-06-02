package org.example.interpreter.model;

public class Print {
    private final String text;

    public Print(String text) {
        this.text = text;
    }

    public void execute() {
        System.out.println(text);
    }
}

