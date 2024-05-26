package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Structure.Expression.IExpression;
import org.example.parser.Structure.Statement.FunctionCall;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class IdentiferAndMethodCallExpression implements IExpression {
    String name;
    FunctionCall methodCall;
    Position position;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
