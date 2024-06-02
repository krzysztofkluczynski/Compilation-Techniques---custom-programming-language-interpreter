package org.example.parser.Structure.Expression.Literals;

import lombok.Value;
import org.example.parser.Structure.Expression.IExpression;
import org.example.interpreter.Visitor;
import org.example.token.Position;

import java.util.List;

@Value
public class LiteralList implements IExpression {
    List<SimpleLiteral> value;
    Position position;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
