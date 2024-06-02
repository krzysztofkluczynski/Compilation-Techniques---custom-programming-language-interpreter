package org.example.parser.Structure.OtherComponents;

import lombok.Value;
import org.example.parser.Node;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class Argument {

    String name;
    TypeDeclaration type;
    Position position;

    public Position getPosition() {
        return null;
    }

}

