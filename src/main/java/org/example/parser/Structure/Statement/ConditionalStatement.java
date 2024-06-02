package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.parser.Structure.OtherComponents.If;
import org.example.interpreter.Visitor;
import org.example.token.Position;

import java.util.List;

@Value
public class ConditionalStatement implements Statement {

    List<If> ifs;
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
