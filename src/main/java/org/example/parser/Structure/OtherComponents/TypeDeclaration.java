package org.example.parser.Structure.OtherComponents;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Enum.Type;
import org.example.interpreter.Visitor;
import org.example.token.Position;

@Value
public class TypeDeclaration implements Node {
    Type type;
    Type firstOptionalParam;
    Type secondOptionalParam;
    Position position;

    public TypeDeclaration(Type basicType, Position pposition) {
        this.type = basicType;
        this.position = pposition;
        firstOptionalParam = null;
        secondOptionalParam = null;
    }

    public TypeDeclaration(Type basicType, Type firstOptionalParam, Position pposition) {
        this.type = basicType;
        this.position = pposition;
        this.firstOptionalParam = firstOptionalParam;
        this.secondOptionalParam = null;
    }

    public TypeDeclaration(Type basicType, Type firstOptionalParam, Type secondOptionalParam, Position pposition) {
        this.type = basicType;
        this.position = pposition;
        this.firstOptionalParam = firstOptionalParam;
        this.secondOptionalParam = secondOptionalParam;
    }

    @Override
    public void accept(Visitor visitor) {

    }
}
