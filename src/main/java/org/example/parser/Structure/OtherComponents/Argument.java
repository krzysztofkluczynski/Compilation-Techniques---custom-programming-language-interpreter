package org.example.parser.Structure.OtherComponents;

import lombok.Value;
import org.example.parser.Node;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class Argument implements Node {

    String name;
    TypeDeclaration type;
    Position position;

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

