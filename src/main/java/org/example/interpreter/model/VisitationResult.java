package org.example.interpreter.model;

public class VisitationResult {
    Variable returnedValue;
    boolean valueReturned;

    public VisitationResult(Variable returnedValue) {
        this.returnedValue = returnedValue;
        this.valueReturned = false;
    }

    public VisitationResult(Variable returnedValue, boolean stopParentContext) {
        this.returnedValue = returnedValue;
        this.valueReturned = stopParentContext;
    }

    public Variable getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(Variable returnedValue) {
        this.returnedValue = returnedValue;
    }

    public boolean wasValueReturned() {
        return valueReturned;
    }

    public void setValueReturned(boolean valueReturned) {
        this.valueReturned = valueReturned;
    }
}

