package org.example.parser.Structure.Expression.Literals;

import lombok.Value;
import org.example.interpreter.Visitor;
import org.example.token.Position;
@Value
public class LiteralInteger implements SimpleLiteral {
    int value;
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
