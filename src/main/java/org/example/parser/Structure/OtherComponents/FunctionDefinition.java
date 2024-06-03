package org.example.parser.Structure.OtherComponents;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.parser.Enum.Type;
import org.example.parser.Node;
import org.example.parser.Structure.Statement.BlockStatement;
import org.example.interpreter.Visitor;
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


    public TypeDeclaration getReturnType() {
        if (returnType == null) {
            return new TypeDeclaration(Type.VOID, position);
        }
        return returnType;
    }

    @Override
    public void accept(Visitor visitor) throws InterpretingException {
        visitor.visit(this);
    }
}
