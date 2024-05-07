package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Visitor;
import org.example.token.Position;

import java.util.List;

@Value
public class BlockStatement implements Node {

    List<Node> instructions;
    Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
