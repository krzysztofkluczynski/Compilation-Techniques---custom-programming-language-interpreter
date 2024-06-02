package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.parser.Structure.OtherComponents.TypeDeclaration;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class CastExpression implements IExpression {
    TypeDeclaration type;
    IExpression expression;
    Position position;


    @Override
    public void accept(Visitor visitor) throws InterpretingException {
        visitor.visit(this);
    }
}
