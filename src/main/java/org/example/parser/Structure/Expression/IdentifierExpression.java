package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.interpreter.Visitor;
import org.example.interpreter.error.NoSuchVariableInterpretingException;
import org.example.token.Position;

@Value
public class IdentifierExpression implements IExpression {
    String name;
    Position position;


    @Override
    public void accept(Visitor visitor) throws NoSuchVariableInterpretingException {
        visitor.visit(this);
    }
}
