package org.example.parser.Structure.Expression.Literals;

import lombok.Value;
import org.example.parser.Structure.Expression.IExpression;
import org.example.interpreter.Visitor;
import org.example.token.Position;

import java.util.Map;

@Value
public class LiteralDictionary implements SimpleLiteral {
    Map<SimpleLiteral, SimpleLiteral> value;
    Position position;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
