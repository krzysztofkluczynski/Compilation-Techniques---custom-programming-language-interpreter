package org.example.parser.Structure.Expression;

import lombok.Value;
import org.example.parser.Enum.AscOrDESC;
import org.example.parser.Structure.Expression.IExpression;
import org.example.parser.Structure.Expression.IdentifierExpression;
import org.example.parser.Visitor;
import org.example.token.Position;

@Value
public class QueryExpression implements IExpression {

    IExpression firstSelectExpression;
    IExpression secondSelectExpression;
    IdentifierExpression fromIdentifer;
    IExpression whereExpression;
    IExpression orderByExpression;
    AscOrDESC ascOrDESC;
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
