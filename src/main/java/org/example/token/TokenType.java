package org.example.token;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum TokenType {


    // Arithmetic operators
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),

    // Comparison operators
    EQUAL("="),
    NOTEQUAL("!="),
    GREATER(">"),
    LESS("<"),
    LESSEQUAL("<="),
    GREATEREQUAL(">="),

    // Logical operators
    AND("and"),
    OR("or"),
    NOT("not"),

    // Types
    INTEGER("int"),
    FLOAT("float"),
    BOOL("bool"),
    STRING("String"),
    DICTIONARY("Dictionary"),
    LIST("List"),
    TUPLE("Tuple"),
    VOID("void"),

    // Brackets
    CURLY_BRACKET_OPEN("{"),
    CURLY_BRACKET_CLOSE("}"),
    BRACKET_OPEN("("),
    BRACKET_CLOSE(")"),
    SQUARE_BRACKET_OPEN("["),
    SQUARE_BRACKET_CLOSE("]"),
    PIPE("|"),

    // Loops and conditional statements
    WHILE("while"),
    FOR("for"),
    IF("if"),
    ELIF("elif"),
    ELSE("else"),

    // Dictionary queries
    SELECT("SELECT"),
    FROM("FROM"),
    WHERE("WHERE"),
    ORDER_BY("ORDER BY"),
    ASCENDING("ASC"),
    DESCENDING("DSC"),

    // Others
    MAIN_FUNCTION("main"),
    FUNCTION("fn"),
    RETURN("return"),
    CAST("$"),
    LAMBDA("=>"),
    STRING_QUOTE("\""),
    SEMICOLON(";"),
    COLON(":"),
    COMMA(","),
    DOT("."),
    IDENTIFIER,
    BOOL_TRUE_LITERAL("True"),
    BOOL_FALSE_LITERAL("False"),
    STRING_LITERAL,
    INT_LITERAL,
    FLOAT_LITERAL,
    END_OF_FILE;

    private String keyword;
    TokenType(String keyword) {
        this.keyword = keyword;
    }

}
