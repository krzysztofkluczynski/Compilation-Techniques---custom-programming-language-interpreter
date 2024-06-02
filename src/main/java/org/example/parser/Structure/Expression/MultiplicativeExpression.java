package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.parser.Enum.MultiplicativeType;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class MultiplicativeExpression implements IExpression {
    IExpression left;
    MultiplicativeType relativeOperand;
    IExpression right;
    Position position;


    @Override
    public void accept(Visitor visitor) throws InterpretingException {
        visitor.visit(this);
    }
}
