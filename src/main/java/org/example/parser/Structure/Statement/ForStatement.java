package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.parser.Enum.Type;
import org.example.parser.Node;
import org.example.parser.Structure.OtherComponents.TypeDeclaration;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class ForStatement implements Node {
    TypeDeclaration type;
    String identifer;
    String collectionIdentifer;
    BlockStatement blockStatement;
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
