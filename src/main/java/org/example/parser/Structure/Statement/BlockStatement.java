package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.interpreter.Visitor;
import org.example.interpreter.error.InterpretingException;
import org.example.token.Position;

import java.util.List;

@Value
public class BlockStatement implements Statement {

    List<Statement> instructions;
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
