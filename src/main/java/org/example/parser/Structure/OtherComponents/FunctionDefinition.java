package org.example.parser.Structure.OtherComponents;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Structure.Statement.BlockStatement;
import org.example.parser.Visitor;
import org.example.token.Position;

import java.util.List;

@ToString(exclude = {"body"})
@EqualsAndHashCode(exclude = "position")
@Value
public class FunctionDefinition implements Node {
    TypeDeclaration returnType;
    String name;
    List<Argument> arguments;
    BlockStatement body;

    Position position;



    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
