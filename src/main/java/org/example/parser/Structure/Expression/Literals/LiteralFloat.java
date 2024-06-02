package org.example.parser.Structure.Expression.Literals;

import lombok.Value;
import org.example.parser.Structure.Expression.IExpression;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class LiteralFloat implements SimpleLiteral {
    Float value;
    Position position;

    @Override
    public Object getValue() {
        return value;
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
