package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Enum.AdditiveType;
import org.example.parser.Enum.RelativeType;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class ArthmeticExpression implements IExpression{
    IExpression left;
    AdditiveType relativeOperand;
    IExpression right;
    Position position;


    @Override
    public void accept(Visitor visitor) {
            visitor.visit(this);
    }
}
