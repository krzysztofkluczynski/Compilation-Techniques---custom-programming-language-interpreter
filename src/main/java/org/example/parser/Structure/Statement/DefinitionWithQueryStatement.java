package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.parser.Structure.OtherComponents.TypeDeclaration;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class DefinitionWithQueryStatement implements Statement{
    TypeDeclaration type;
    String IdentifierName;
    QueryStatement queryStatement;
    Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

