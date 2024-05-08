package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class OrExpression implements IExpression {
    IExpression left;
    IExpression right;

    Position position;

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public void accept(Visitor visitor) {

    }
}
