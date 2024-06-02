package org.example.interpreter.model;

import org.example.interpreter.error.InterpretingException;
import org.example.interpreter.error.VariableValueTypeInterpretingException;
import org.example.parser.Enum.Type;

public class Variable {
    Type variableType;
    String name = null;
    Object value = null;

    public Variable(Type variableType, String name) {
        this.variableType = variableType;
        this.name = name;
    }

    public Variable(Type variableType, String name, Object value) throws InterpretingException {
        this.variableType = variableType;
        this.name = name;
        this.value = value;
        assertValueIsCorrectType();
    }

    public Variable(Type variableType, Object value) {
        this.variableType = variableType;
        this.value = value;
    }

    public Type getVariableType() {
        return variableType;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    private void assertValueIsCorrectType() throws VariableValueTypeInterpretingException {
        boolean throwException = false;
        switch (variableType) {
            case FLOAT:
                throwException = !(value instanceof Float);
                break;
            case INT:
                throwException = !(value instanceof Integer);
                break;
            case BOOL:
                throwException = !(value instanceof Boolean);
                break;
            case STRING:
                throwException = !(value instanceof String);
                break;
        }
        if (throwException) {
            throw new VariableValueTypeInterpretingException(this);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

