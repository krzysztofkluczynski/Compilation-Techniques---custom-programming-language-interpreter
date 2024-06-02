package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.parser.Structure.Expression.IExpression;
import org.example.interpreter.Visitor;
import org.example.token.Position;

import java.util.List;

@Value
public class FunctionCall implements Statement, IExpression {
    String name;
    List<IExpression> arguments;
    Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void accept(Visitor visitor) throws InterpretingException {
        visitor.visit(this);
    }
}
