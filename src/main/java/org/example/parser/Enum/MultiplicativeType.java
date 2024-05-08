package org.example.parser.Enum;

public enum MultiplicativeType {
        MUL("*"),
        DIV("/");

        private String symbol;

    MultiplicativeType(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        public static org.example.parser.Enum.MultiplicativeType getExpressionOperandBySymbol(String symbol) {
            if (symbol != null) {
                for (org.example.parser.Enum.MultiplicativeType operand : org.example.parser.Enum.MultiplicativeType.values()) {
                    if (operand.getSymbol().equals(symbol)) {
                        return operand;
                    }
                }
            }
            return null;
        }
    }
