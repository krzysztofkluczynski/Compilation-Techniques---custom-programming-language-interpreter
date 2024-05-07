package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class Expression implements Node {
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
