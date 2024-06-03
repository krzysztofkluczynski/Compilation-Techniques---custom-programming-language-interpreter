package org.example.interpreter.model;

import org.example.interpreter.error.LocalVariableRepeatedInterpretingException;
import org.example.interpreter.error.NoSuchVariableInterpretingException;
import org.example.interpreter.error.VariableValueTypeInterpretingException;
import org.example.parser.Enum.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionCallContext extends Scope {
    Type functionType;
    String name;
    List<Scope> scopes;

    public FunctionCallContext(Type functionType, String name, Map<String, Variable> localVariables) {
        this.functionType = functionType;
        this.name = name;
        this.scopes = new ArrayList<>();
        this.localVariables = localVariables;
    }

    public void addScope(Scope scope) {
        scopes.add(scope);
    }

    public void removeLastScope() {
        scopes.remove(scopes.size() - 1);
    }

    public boolean hasMoreScopes() {
        return !scopes.isEmpty();
    }

    public Type getFunctionType() {
        return functionType;
    }

    public String getName() {
        return name;
    }

    public Variable getVariable(String name) {
        for (int idx = scopes.size() - 1; idx >= 0; idx--) {
            Variable possibleVariable = scopes.get(idx).getLocalVariable(name);
            if (possibleVariable != null) {
                return possibleVariable;
            }
        }
        return localVariables.get(name);
    }

    public void addLocalVariableToLastScope(Variable variable) throws LocalVariableRepeatedInterpretingException {
        scopes.get(scopes.size() - 1).addLocalVariable(variable);
    }

    public void updateVariable(String variableName, Object newValue) throws NoSuchVariableInterpretingException, VariableValueTypeInterpretingException {
        Variable variable = getVariable(variableName);
        if (variable == null) {
            throw new NoSuchVariableInterpretingException(variableName);
        }
        variable.setValue(newValue);
    }
}
