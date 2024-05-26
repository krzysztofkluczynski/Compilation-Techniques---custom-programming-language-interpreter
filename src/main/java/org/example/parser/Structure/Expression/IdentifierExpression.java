package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class IdentifierExpression implements IExpression {
    String name;
    Position position;


    @Override
    public void accept(Visitor visitor) {

    }
}
