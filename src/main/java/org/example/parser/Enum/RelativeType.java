package org.example.parser.Enum;

public enum RelativeType {
    EQUAL("=="),
    NOT_EQUAL("!="),
    LESS("<"),
    MORE(">"),
    LESSEQUAL("<="),
    MOREEQUAL(">=");

    private String symbol;

    RelativeType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static RelativeType getConditionOperandBySymbol(String symbol) {
        if (symbol != null) {
            for (RelativeType operand : RelativeType.values()) {
                if (operand.getSymbol().equals(symbol)) {
                    return operand;
                }
            }
        }
        return null;
    }
}

