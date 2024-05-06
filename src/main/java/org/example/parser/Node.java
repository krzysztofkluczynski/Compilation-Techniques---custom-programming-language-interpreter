package org.example.parser;

import org.example.token.Position;

import java.util.List;

public interface Node {
    Position getPosition();

    void accept(Visitor visitor);

//    default String print() {
//        return null;
//    }
}
