package org.example.parser.Structure.Statement;

import lombok.Value;
import org.example.interpreter.Visitor;
import org.example.interpreter.error.NoSuchVariableInterpretingException;
import org.example.token.Position;

@Value
public class AssignmentWithQueryStatement implements Statement{
    String IdentifierName;
    org.example.parser.Structure.Expression.QueryExpression QueryExpression;
    Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void accept(Visitor visitor) throws NoSuchVariableInterpretingException {
        visitor.visit(this);
    }
}
