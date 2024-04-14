import org.example.lexer.Lexer;
import org.example.lexer.LexerImpl;
import org.example.lexer.error.*;
import org.example.reader.DataStreamInputReader;
import org.example.token.IntegerToken;
import org.example.token.Token;
import org.example.token.TokenType;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LexerTests {
    @Test
    public void testSimpleDataTypes() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                   int a = 3; float b = 3.14; bool c = true; String x = "ala ma kota123";
                   """
        );
        LexerImpl lexer = new LexerImpl(reader);


        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Token tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(3, (Integer) tokenInt.getValue());

        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        Assert.assertEquals(TokenType.FLOAT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Token tokenFloat = lexer.next();
        Assert.assertEquals(TokenType.FLOAT_LITERAL, tokenFloat.getType());
        Assert.assertEquals(3.14f, (Float) tokenFloat.getValue());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        Assert.assertEquals(TokenType.BOOL, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Token tokenBool = lexer.next();
        Assert.assertEquals(TokenType.BOOL_LITERAL, tokenBool.getType());
        Assert.assertEquals(true, tokenBool.getValue());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        Assert.assertEquals(TokenType.STRING, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Token tokenString = lexer.next();
        Assert.assertEquals(TokenType.STRING_LITERAL, tokenString.getType());
        Assert.assertEquals("ala ma kota123", tokenString.getValue());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

    }

    @Test
    public void testArithmeticAndAssignments() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                int x;  x + 4;  23 - 5; 6 * 4; 6.5 / 76.5; x = 5; 
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test "int x;"
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test "x + 4;"
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.PLUS, lexer.next().getType());
        Token tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(4, (Integer) tokenInt.getValue());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test "23 - 5;"
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(23, (Integer) tokenInt.getValue());
        Assert.assertEquals(TokenType.MINUS, lexer.next().getType());
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(5, (Integer) tokenInt.getValue());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test "6 * 4;"
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(6, (Integer) tokenInt.getValue());
        Assert.assertEquals(TokenType.MULTIPLY, lexer.next().getType());
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(4, (Integer) tokenInt.getValue());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test "6.5 / 76.5;"
        Token tokenFloat = lexer.next();
        Assert.assertEquals(TokenType.FLOAT_LITERAL, tokenFloat.getType());
        Assert.assertEquals(6.5f, (Float) tokenFloat.getValue());
        Assert.assertEquals(TokenType.DIVIDE, lexer.next().getType());
        tokenFloat = lexer.next();
        Assert.assertEquals(TokenType.FLOAT_LITERAL, tokenFloat.getType());
        Assert.assertEquals(76.5f, (Float) tokenFloat.getValue());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test "x = 5;"
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(5, (Integer) tokenInt.getValue());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
    }
    @Test
    public void testComplexExpressions() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                (a_b != 3) ( test == 2) (test2  > 2); "string" <= "string2"; "string3" >= 5;  321 > 320; 567 < 1234 
                cond_first and condition_second or conditon_third not condition_four
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test comparison expressions within parentheses
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.NOTEQUAL, lexer.next().getType());
        Token tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());

        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL_COMP, lexer.next().getType());
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());

        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());

        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test string comparison
        Token tokenString = lexer.next();
        Assert.assertEquals(TokenType.STRING_LITERAL, tokenString.getType());
        Assert.assertEquals("string", tokenString.getValue());
        Assert.assertEquals(TokenType.LESSEQUAL, lexer.next().getType());
        tokenString = lexer.next();
        Assert.assertEquals(TokenType.STRING_LITERAL, tokenString.getType());
        Assert.assertEquals("string2", tokenString.getValue());

        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Numerical comparisons and string comparison with numeric value
        tokenString = lexer.next();
        Assert.assertEquals(TokenType.STRING_LITERAL, tokenString.getType());
        Assert.assertEquals("string3", tokenString.getValue());
        Assert.assertEquals(TokenType.GREATEREQUAL, lexer.next().getType());
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());

        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());

        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());

        // Test logical expressions
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.AND, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.OR, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.NOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
    }

    @Test
    public void testDataHandlingFeatures() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                Dictionary < String , int > var_dict = |
                    "dog": 3,
                    "cat": 4,
                    "cow": 5,
                    "hamster": 6 |;
                var_tuple.get(0) // Getting the first item from the tuple
                var_tuple.set(0, 2) // Setting the value at index 0 to 2
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test Dictionary with type annotations
        Assert.assertEquals(TokenType.DICTIONARY, lexer.next().getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.PIPE, lexer.next().getType());

        Assert.assertEquals(TokenType.STRING_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COLON, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COLON, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COLON, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COLON, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.PIPE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, lexer.next().getType());

        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, lexer.next().getType());
    }
}
