package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.interpreter.error.LocalVariableRepeatedInterpretingException;
import org.example.parser.Structure.OtherComponents.TypeDeclaration;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class DeclarationStatement implements Statement {
    TypeDeclaration type;
    String name;
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
