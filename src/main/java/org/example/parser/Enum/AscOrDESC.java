package org.example.parser.Enum;

public enum AscOrDESC {
    ASC("ASC"),
    DESC("DESC");

    private String symbol;

    AscOrDESC(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static AscOrDESC getBySymbol(String symbol) {
        if (symbol != null) {
            for (AscOrDESC operand : AscOrDESC.values()) {
                if (operand.getSymbol().equals(symbol)) {
                    return operand;
                }
            }
        }
        return null;
    }
}
