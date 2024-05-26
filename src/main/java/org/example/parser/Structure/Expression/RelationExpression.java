package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Enum.RelativeType;
import org.example.parser.Visitor;
import org.example.token.Position;

import java.beans.Expression;

@Value
public class RelationExpression implements IExpression {
    IExpression left;
    RelativeType relativeOperand;
    IExpression right;
    Position position;


    @Override
    public void accept(Visitor visitor) {

    }
}
