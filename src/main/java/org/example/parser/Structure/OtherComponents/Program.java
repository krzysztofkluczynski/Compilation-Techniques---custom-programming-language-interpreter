package org.example.parser.Structure.OtherComponents;

import lombok.Value;
import org.example.interpreter.error.InterpretingException;
import org.example.parser.Node;
import org.example.interpreter.Visitor;
import org.example.token.Position;

import java.util.Map;

@Value
public class Program implements Node {

    Map<String, FunctionDefinition> functionDefinitions;


    @Override
    public Position getPosition() {
        return new Position(1, 0);
    }


    @Override
    public void accept(Visitor visitor) throws InterpretingException {
        visitor.visit(this);
    }


}
