package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.interpreter.Visitor;
import org.example.interpreter.error.InterpretingException;
import org.example.token.Position;

@Value
public class NegatedExpression implements IExpression {
    IExpression expression;

    Position position;

    @Override
    public void accept(Visitor visitor) throws InterpretingException {
        visitor.visit(this);
    }
}
