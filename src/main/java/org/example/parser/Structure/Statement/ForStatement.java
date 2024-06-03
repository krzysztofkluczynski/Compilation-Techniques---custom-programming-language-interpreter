package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.parser.Structure.OtherComponents.TypeDeclaration;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class ForStatement implements Statement {
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
    public void accept(Visitor visitor) throws InterpretingException {
        visitor.visit(this);
    }
}
