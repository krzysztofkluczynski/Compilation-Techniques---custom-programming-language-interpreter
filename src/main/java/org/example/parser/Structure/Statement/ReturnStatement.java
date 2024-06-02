package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.parser.Structure.Expression.IExpression;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class ReturnStatement implements Statement {
    IExpression expression;
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
