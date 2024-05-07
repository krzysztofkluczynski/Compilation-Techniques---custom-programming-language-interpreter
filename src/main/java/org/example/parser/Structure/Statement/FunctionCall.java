package org.example.parser.Structure.Statement;

import org.example.parser.Node;
import org.example.parser.Visitor;
import org.example.token.Position;

public class FunctionCall implements Node {
    Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.accept(this);
    }
}
