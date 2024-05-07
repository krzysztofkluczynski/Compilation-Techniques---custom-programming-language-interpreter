package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Structure.Expression.Expression;
import org.example.parser.Structure.OtherComponents.Elif;
import org.example.parser.Visitor;
import org.example.token.Position;

import java.util.List;

@Value
public class ConditionalStatement implements Statement {
    Expression ifExpression;
    BlockStatement ifBlock;
    List<Elif> elifs;
    BlockStatement elseBlock;

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
