package org.example.parser.Structure.OtherComponents;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Structure.Expression.IExpression;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class SelectClause implements Node {
    IExpression firstExpression;
    IExpression secondExpression;
    Position position;

    @Override
    public void accept(Visitor visitor) {
        visitor.accept(this);
    }
}
