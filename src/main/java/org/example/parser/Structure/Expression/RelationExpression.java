package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.parser.Enum.RelativeType;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class RelationExpression implements IExpression {
    IExpression left;
    RelativeType relativeOperand;
    IExpression right;
    Position position;


    @Override
    public void accept(Visitor visitor) throws InterpretingException {
        visitor.visit(this);
    }
}
