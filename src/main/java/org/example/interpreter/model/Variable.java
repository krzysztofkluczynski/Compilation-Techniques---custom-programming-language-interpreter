package org.example.interpreter.model;

import org.example.interpreter.error.InterpretingException;
import org.example.interpreter.error.VariableValueTypeInterpretingException;
import org.example.parser.Enum.Type;
import org.example.parser.Structure.Expression.Literals.LiteralTuple;

import java.util.List;
import java.util.Map;

public class Variable {
    Type variableType;
    Type optionalOne = null;
    Type optionalTwo = null;
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

    public Variable(Type variableType, Type optionalOne, String name, Object value) throws InterpretingException {
        this.variableType = variableType;
        this.name = name;
        this.value = value;
        this.optionalOne = optionalOne;
        assertValueIsCorrectType();
    }

    public Variable(Type variableType, Type optionalOne, Type optionalTwo, String name, Object value) throws InterpretingException {
        this.variableType = variableType;
        this.name = name;
        this.value = value;
        this.optionalOne = optionalOne;
        this.optionalTwo = optionalTwo;
        assertValueIsCorrectType();
    }

    public Variable(Type variableType, Type optionalOne, String name) throws InterpretingException {
        this.variableType = variableType;
        this.name = name;
        this.value = value;
        this.optionalOne = optionalOne;
        assertValueIsCorrectType();
    }

    public Variable(Type variableType, Type optionalOne, Type optionalTwo, String name) throws InterpretingException {
        this.variableType = variableType;
        this.name = name;
        this.value = value;
        this.optionalOne = optionalOne;
        this.optionalTwo = optionalTwo;
        assertValueIsCorrectType();
    }

    public Variable(Type variableType, Object value) {
        this.variableType = variableType;
        this.value = value;
    }

    //w przypadku gdzie chcemu zwrocic wartosc zmiennej string
    public Variable(String value) {
        this.variableType = Type.STRING;
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
            case TUPLE:
                throwException = !(value instanceof Pair);
                if (throwException) {
                    break;
                }
                Pair newPair = (Pair) value;
                checkForComplex(newPair.getFirst(), optionalOne);
                checkForComplex(newPair.getFirst(), optionalOne);
                break;
            case LIST:
                throwException = !(value instanceof List);
                if (throwException) {
                    break;
                }
                List<Object> list = (List<Object>) value;
                for (Object element : list) {
                    checkForComplex(element, optionalOne);
                }
                break;
            case DICTIONARY:
                throwException = !(value instanceof Map);
                if (throwException) {
                    break;
                }
                Map<Object, Object> map = (Map<Object, Object>) value;
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    checkForComplex(entry.getKey(), optionalOne);
                    checkForComplex(entry.getValue(), optionalTwo);
                }
                break;
        }
        if (throwException) {
            throw new VariableValueTypeInterpretingException(this);
        }
    }

    private void checkForComplex(Object paramValue, Type expectedType) throws VariableValueTypeInterpretingException {
        boolean throwException = false;
        switch (expectedType) {
            case FLOAT:
                throwException = !(paramValue instanceof Float);
                break;
            case INT:
                throwException = !(paramValue instanceof Integer);
                break;
            case BOOL:
                throwException = !(paramValue instanceof Boolean);
                break;
            case STRING:
                throwException = !(paramValue instanceof String);
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

