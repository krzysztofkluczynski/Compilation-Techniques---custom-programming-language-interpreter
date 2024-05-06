package org.example.parser.Structure;

import lombok.Value;
import org.example.parser.Node;
import org.example.parser.Structure.utils.Type;
import org.example.parser.Visitor;
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
    public Position getPosition() {
        return null;
    }

    @Override
    public void accept(Visitor visitor) {

    }
}
