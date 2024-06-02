package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.parser.Enum.AdditiveType;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class ArthmeticExpression implements IExpression{
    IExpression left;
    AdditiveType relativeOperand;
    IExpression right;
    Position position;


    @Override
    public void accept(Visitor visitor) throws InterpretingException {
            visitor.visit(this);
    }
}
