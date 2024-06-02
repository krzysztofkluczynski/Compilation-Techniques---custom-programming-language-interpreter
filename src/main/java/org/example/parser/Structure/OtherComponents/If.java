package org.example.parser.Structure.OtherComponents;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Structure.Expression.IExpression;
import org.example.parser.Structure.Statement.BlockStatement;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class If {
    IExpression expression;
    BlockStatement blockStatement;
    Position position;

    public Position getPosition() {
        return position;
    }

//    @Override
//    public void accept(Visitor visitor) {
//        visitor.visit(this);
//    }

}
