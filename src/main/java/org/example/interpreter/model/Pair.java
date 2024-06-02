package org.example.interpreter.model;

public class Pair {
    private Object first;
    private Object second;

    public Pair(Object key, Object value) {
        this.first = key;
        this.second = value;
    }

    public Object getFirst() {
        return first;
    }

    public Object getSecond() {
        return second;
    }

    public void setFirst(Object first) {
        this.first = first;
    }

    public void setSecond(Object second) {
        this.second = second;
    }
}
