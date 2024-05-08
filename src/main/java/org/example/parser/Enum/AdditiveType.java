package org.example.parser.Enum;

public enum  AdditiveType{
    ADD("+"),
    SUB("-");

    private String symbol;

    AdditiveType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static AdditiveType getExpressionOperandBySymbol(String symbol) {
        if (symbol != null) {
            for (AdditiveType operand : AdditiveType.values()) {
                if (operand.getSymbol().equals(symbol)) {
                    return operand;
                }
            }
        }
        return null;
    }
}
