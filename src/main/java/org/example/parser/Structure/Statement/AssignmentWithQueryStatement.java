package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class AssignmentWithQueryStatement implements Statement{
    String IdentifierName;
    org.example.parser.Structure.Expression.QueryExpression QueryExpression;
    Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.accept(this);
    }
}
