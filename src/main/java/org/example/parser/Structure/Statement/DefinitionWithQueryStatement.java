package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.parser.Structure.Expression.QueryExpression;
import org.example.parser.Structure.OtherComponents.TypeDeclaration;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class DefinitionWithQueryStatement implements Statement{
    TypeDeclaration type;
    String IdentifierName;
    QueryExpression queryExpression;
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

