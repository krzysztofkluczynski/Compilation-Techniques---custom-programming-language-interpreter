package org.example.parser.Enum;

public enum Type {
    BOOL("bool"),
    INT("int"),
    FLOAT("float"),
    STRING("String"),
    LIST("List"),
    TUPLE("Tuple"),
    DICTIONARY("Dictionary"),
    VOID("void");


    private final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Type getTypeByName(String name) {
        if (name != null) {
            for (Type type : Type.values()) {
                if (name.equals(type.name)) {
                    return type;
                }
            }
        }
        return null;
    }
}
