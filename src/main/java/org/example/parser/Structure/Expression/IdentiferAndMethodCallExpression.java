package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Structure.Statement.FunctionCall;
import org.example.interpreter.Visitor;
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
