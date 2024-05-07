package org.example.parser.Structure.OtherComponents;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Structure.Expression.Expression;
import org.example.parser.Structure.Statement.BlockStatement;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class Elif implements Node {
    Expression expression;
    BlockStatement blockStatement;
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
