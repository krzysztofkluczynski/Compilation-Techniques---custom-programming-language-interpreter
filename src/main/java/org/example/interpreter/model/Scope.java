package org.example.interpreter.model;

import org.example.interpreter.error.LocalVariableRepeatedInterpretingException;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    Map<String, Variable> localVariables;

    public Scope() {
        this.localVariables = new HashMap<>();
    }

    public Scope(Map<String, Variable> localVariables) {
        this.localVariables = localVariables;
    }

    public Variable getLocalVariable(String name) {
        return localVariables.get(name);
    }

    public void addLocalVariable(Variable variable) throws LocalVariableRepeatedInterpretingException {
        if (localVariables.containsKey(variable.name)) {
            throw new LocalVariableRepeatedInterpretingException(variable);
        }
        localVariables.put(variable.name, variable);
    }
}

