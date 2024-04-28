package org.example.lexer.error;

public class NumberMaxValueExceededException extends Exception {
        private final String value;

        public NumberMaxValueExceededException(String msg, String value) {
            super(msg);
            this.value = value;
        }

    public String getValue() {
        return value;
    }

}
