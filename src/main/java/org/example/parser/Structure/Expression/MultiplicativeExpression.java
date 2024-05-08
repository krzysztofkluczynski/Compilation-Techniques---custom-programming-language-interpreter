package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Enum.AdditiveType;
import org.example.parser.Enum.MultiplicativeType;
import org.example.parser.Structure.Expression.IExpression;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class MultiplicativeExpression implements IExpression {
    IExpression left;
    MultiplicativeType relativeOperand;
    IExpression right;
    Position position;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
