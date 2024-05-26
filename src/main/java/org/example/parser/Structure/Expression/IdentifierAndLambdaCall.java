package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Structure.OtherComponents.Argument;
import org.example.parser.Visitor;
import org.example.token.Position;

import java.util.List;

@Value
public class IdentifierAndLambdaCall implements IExpression {
    String name;
    String methodName = "sort";
    List<Argument> argumentList;
    IExpression expression;
    Position position;

    @Override
    public void accept(Visitor visitor) {

    }
}
