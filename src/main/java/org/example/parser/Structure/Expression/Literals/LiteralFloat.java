package org.example.parser.Structure.Expression.Literals;

import lombok.Value;
import org.example.parser.Structure.Expression.IExpression;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class LiteralFloat implements IExpression {
    Float value;
    Position position;


    @Override
    public void accept(Visitor visitor) {
        visitor.accept(this);
    }
}
