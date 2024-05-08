package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Structure.Expression.IExpression;
import org.example.parser.Visitor;
import org.example.token.Position;

import java.beans.Expression;

@Value
public class NegatedExpression implements IExpression {
    IExpression expression;

    Position position;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
