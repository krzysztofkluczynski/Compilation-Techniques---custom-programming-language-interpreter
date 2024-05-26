package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Structure.Expression.IExpression;
import org.example.parser.Visitor;
import org.example.token.Position;

import java.beans.Expression;

@Value
public class IdentifierandLambdaCall implements IExpression {
    String name;
    Position position;

    @Override
    public void accept(Visitor visitor) {

    }
}
