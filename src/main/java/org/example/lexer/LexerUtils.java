package org.example.lexer;

import org.example.token.TokenType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LexerUtils {

    private static final Map<String, TokenType> keywordTokens = initializeTokens();

    // Method to initialize the map using Map.ofEntries
    private static Map<String, TokenType> initializeTokens() {
        return Map.ofEntries(
                Map.entry("+", TokenType.PLUS),
                Map.entry("-", TokenType.MINUS),
                Map.entry("*", TokenType.MULTIPLY),
                Map.entry("/", TokenType.DIVIDE),
                Map.entry("=", TokenType.EQUAL),
                Map.entry("==", TokenType.EQUAL_COMP),
                Map.entry("!=", TokenType.NOTEQUAL),
                Map.entry(">", TokenType.GREATER),
                Map.entry("<", TokenType.LESS),
                Map.entry("<=", TokenType.LESSEQUAL),
                Map.entry(">=", TokenType.GREATEREQUAL),
                Map.entry("{", TokenType.CURLY_BRACKET_OPEN),
                Map.entry("}", TokenType.CURLY_BRACKET_CLOSE),
                Map.entry("(", TokenType.BRACKET_OPEN),
                Map.entry(")", TokenType.BRACKET_CLOSE),
                Map.entry("[", TokenType.SQUARE_BRACKET_OPEN),
                Map.entry("]", TokenType.SQUARE_BRACKET_CLOSE),
                Map.entry("|", TokenType.PIPE),
                Map.entry(";", TokenType.SEMICOLON),
                Map.entry(":", TokenType.COLON),
                Map.entry(",", TokenType.COMMA),
                Map.entry(".", TokenType.DOT),
                Map.entry("$", TokenType.CAST),
                Map.entry("=>", TokenType.LAMBDA),
                // Loops and conditional statements
                Map.entry("while", TokenType.WHILE),
                Map.entry("for", TokenType.FOR),
                Map.entry("if", TokenType.IF),
                Map.entry("elif", TokenType.ELIF),
                Map.entry("else", TokenType.ELSE),
                // Dictionary queries
                Map.entry("SELECT", TokenType.SELECT),
                Map.entry("FROM", TokenType.FROM),
                Map.entry("WHERE", TokenType.WHERE),
                Map.entry("ORDER BY", TokenType.ORDER_BY),
                Map.entry("ASC", TokenType.ASCENDING),
                Map.entry("DSC", TokenType.DESCENDING),
                // Others
                Map.entry("main", TokenType.MAIN_FUNCTION),
                Map.entry("fn", TokenType.FUNCTION),
                Map.entry("return", TokenType.RETURN),
                // Logical operators
                Map.entry("and", TokenType.AND),
                Map.entry("or", TokenType.OR),
                Map.entry("not", TokenType.NOT),
                // Types
                Map.entry("int", TokenType.INTEGER),
                Map.entry("float", TokenType.FLOAT),
                Map.entry("bool", TokenType.BOOL),
                Map.entry("String", TokenType.STRING),
                Map.entry("Dictionary", TokenType.DICTIONARY),
                Map.entry("List", TokenType.LIST),
                Map.entry("Tuple", TokenType.TUPLE),
                Map.entry("void", TokenType.VOID)
        );
    }

    public static TokenType getTokenType(String keyword) {
        return keywordTokens.get(keyword);
    }
}
