package org.example.parser.Structure;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Structure.FunctionDefinition;
import org.example.parser.Visitor;
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
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


}
