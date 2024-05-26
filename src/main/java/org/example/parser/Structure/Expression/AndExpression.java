package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Visitor;
import org.example.token.Position;

import java.beans.Expression;

@Value
public class AndExpression implements IExpression {
    IExpression left;
    IExpression right;
    Position position;

    @Override
    public void accept(Visitor visitor) {
    }
}
