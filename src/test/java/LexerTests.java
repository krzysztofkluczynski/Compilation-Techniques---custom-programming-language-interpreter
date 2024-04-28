import org.example.lexer.Lexer;
import org.example.lexer.LexerImpl;
import org.example.lexer.error.*;
import org.example.reader.DataStreamInputReader;
import org.example.token.IntegerToken;
import org.example.token.Token;
import org.example.token.TokenType;
import org.junit.jupiter.api.Assertions;
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
    public void testFullFeaturesIncludingComments() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                Dictionary<String, int> var_dict = |
                    "dog": 3,
                    "cat": 4,
                    "cow": 5,
                    "hamster": 6 
                 |;
                var_tuple.get(0) // Getting the first item from the tuple
                var_tuple.set(0, 2) // Setting the value at index 0 to 2
                var_dict.sort(( Tuple<String, int> a, Tuple<String, int> b) => a.get(1) > b.get(1)); // if value a is greater than value b, then a should be first in order
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

        // Test dictionary contents
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

        // Test tuple method get() and comments
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Token commentToken = lexer.next();
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, commentToken.getType());

        // Test tuple method set() and comments
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        commentToken = lexer.next();
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, commentToken.getType());

        // Test lambda expression in sort function
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.TUPLE, lexer.next().getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.TUPLE, lexer.next().getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.LAMBDA, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        commentToken = lexer.next();
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, commentToken.getType());
    }

    @Test
    public void testTuplesAndComments() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                Tuple<String, int> var_tuple = ("dog", 3);
                
                var_tuple.get(0) 
                var_tuple.set(0, 2)
                
                /* multi
                line
                comment
                */
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test Tuple with type annotations
        Assert.assertEquals(TokenType.TUPLE, lexer.next().getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test tuple method get()
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());

        // Test tuple method set()
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());

        // Test multi-line comment
        Assert.assertEquals(TokenType.MULTI_LINE_COMMENT, lexer.next().getType());
    }

    @Test
    public void testListOperations() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                List<int> var_list = [1, 2, 3, 4, 5];
                
                var_list.add(12); // Adding an element to the end of the list
                var_list.delete(0); // Removing the first element from the list
                var_list.get(0); // Getting the first element from the list
                var_list.set(0, 2); // Setting the value at index 0 to 2
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test List declaration with type annotations
        Assert.assertEquals(TokenType.LIST, lexer.next().getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SQUARE_BRACKET_OPEN, lexer.next().getType());
        for (int i = 1; i <= 5; i++) {
            Token tokenInt = lexer.next();
            Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
            Assert.assertEquals(i, (Integer) tokenInt.getValue());
            if (i < 5) {
                Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
            }
        }
        Assert.assertEquals(TokenType.SQUARE_BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test adding an element to the list
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Token tokenInt = lexer.next();
        Assert.assertEquals(TokenType.INT_LITERAL, tokenInt.getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, lexer.next().getType());  // Comment token

        // Test deleting the first element from the list
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, lexer.next().getType());  // Comment token

        // Test getting the first element from the list
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, lexer.next().getType());  // Comment token

        // Test setting the value at index 0 in the list
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
    }
    @Test
    public void testFunctionAndOperations() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                // Function that increments an int
                fn void increment(int x) {
                    x = x + 1;
                 }

                fn int main() {
                    int x = 256;
                    increment(x);
                    print($String x); // x = 257
                    return 0;
                 }
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test comment above increment function
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, lexer.next().getType());

        // Test function declaration for 'increment'
        Assert.assertEquals(TokenType.FUNCTION, lexer.next().getType());
        Assert.assertEquals(TokenType.VOID, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_OPEN, lexer.next().getType());

        // Test operation inside 'increment'
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.PLUS, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Close 'increment' function
        Assert.assertEquals(TokenType.CURLY_BRACKET_CLOSE, lexer.next().getType());

        // Test function declaration for 'main'
        Assert.assertEquals(TokenType.FUNCTION, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.MAIN_FUNCTION, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_OPEN, lexer.next().getType());

        // Test variable declaration in 'main'
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test function call and operations in 'main'
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test print and casting in 'main'
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.CAST, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test inline comment
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, lexer.next().getType());

        // Test return in 'main'
        Assert.assertEquals(TokenType.RETURN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Close 'main' function
        Assert.assertEquals(TokenType.CURLY_BRACKET_CLOSE, lexer.next().getType());
    }

    @Test
    public void testSQLLikeQueryParsing() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                List<String> query_result2 = 
                              SELECT 
                          (var_dict.key)
                              FROM
                          var_dict
                              WHERE
                          (var_dict.value != 3 and var_dict.value > 0)
                              ORDER_BY
                          var_dict.value
                              DSC
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test List with type annotations
        Assert.assertEquals(TokenType.LIST, lexer.next().getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        Assert.assertEquals(TokenType.STRING, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());

        // Test SQL-like SELECT statement
        Assert.assertEquals(TokenType.SELECT, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());

        // Test SQL-like FROM clause
        Assert.assertEquals(TokenType.FROM, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());

        // Test SQL-like WHERE clause
        Assert.assertEquals(TokenType.WHERE, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.NOTEQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.AND, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());

        // Test SQL-like ORDER BY clause
        Assert.assertEquals(TokenType.ORDER_BY, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.DOT, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());

        // Test SQL-like DESC keyword
        Assert.assertEquals(TokenType.DESCENDING, lexer.next().getType());
    }

    @Test
    public void testConditionalStructures() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                int a = 2;
                
                int b;
                
                if (a == 2) {
                
                      b = 2;
                
                } elif (a < 2) {
                
                      b = -3;  
                
                } else {
                      b = 3;
                 }
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test initial integer declaration and assignment
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test second integer declaration
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        // Test if statement
        Assert.assertEquals(TokenType.IF, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL_COMP, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_CLOSE, lexer.next().getType());

        // Test elif statement
        Assert.assertEquals(TokenType.ELIF, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.MINUS, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_CLOSE, lexer.next().getType());

        // Test else statement
        Assert.assertEquals(TokenType.ELSE, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_CLOSE, lexer.next().getType());
    }

    @Test
    public void testControlFlowAndListOperations() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                List<int> list = [1, 2, 3, 4, 5, 6, 7, 8];
                
                for (int x : list) {  // Increment each element in the list
                    x = x + 1;
                 }
                
                int x = 2;
                
                while (x != 5) {
                    x = x + 1;
                 }
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        Assert.assertEquals(TokenType.LIST, lexer.next().getType());
        Assert.assertEquals(TokenType.LESS, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.GREATER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SQUARE_BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        for (int i = 0; i < 7; i++) { // Check the remaining integers
            Assert.assertEquals(TokenType.COMMA, lexer.next().getType());
            Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        }
        Assert.assertEquals(TokenType.SQUARE_BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());

        Assert.assertEquals(TokenType.FOR, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.COLON, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_OPEN, lexer.next().getType());
        Token commentToken = lexer.next();  // Handle the comment
        Assert.assertEquals(TokenType.ONE_LINE_COMMENT, commentToken.getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.PLUS, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_CLOSE, lexer.next().getType());

        Assert.assertEquals(TokenType.INTEGER, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.WHILE, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.NOTEQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.BRACKET_CLOSE, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_OPEN, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.EQUAL, lexer.next().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.next().getType());
        Assert.assertEquals(TokenType.PLUS, lexer.next().getType());
        Assert.assertEquals(TokenType.INT_LITERAL, lexer.next().getType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.next().getType());
        Assert.assertEquals(TokenType.CURLY_BRACKET_CLOSE, lexer.next().getType());
    }

    @Test
    public void testIdentifierTooLong() throws FileNotFoundException {
        DataStreamInputReader reader = new DataStreamInputReader("extremely_long_identifier_name_that_exceeds_the_maximum_allowed_lengthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        LexerImpl lexer = new LexerImpl(reader);
        Assertions.assertThrows(IdentifierTooLongException.class, lexer::next);
    }


    @Test
    public void testNumberMaxValueExceeded() throws FileNotFoundException {
        DataStreamInputReader reader = new DataStreamInputReader("100000001");
        LexerImpl lexer = new LexerImpl(reader);
        Assertions.assertThrows(NumberMaxValueExceededException.class, lexer::next);
    }

    @Test
    public void testReachedEOFException() throws FileNotFoundException {
        DataStreamInputReader reader = new DataStreamInputReader("\"some text");
        LexerImpl lexer = new LexerImpl(reader);
        Assertions.assertThrows(ReachedEOFException.class, lexer::next);
    }

    @Test
    public void testStringMaxSizeExceeded() throws FileNotFoundException {
        DataStreamInputReader reader = new DataStreamInputReader("\"" + "a".repeat(10001) + "\"");
        LexerImpl lexer = new LexerImpl(reader);
        Assertions.assertThrows(StringMaxSizeExceeded.class, lexer::next);
    }

    @Test
    public void testUnkownTokenException() throws FileNotFoundException {
        DataStreamInputReader reader = new DataStreamInputReader("@@unknown@@");
        LexerImpl lexer = new LexerImpl(reader);
        Assertions.assertThrows(UnkownTokenException.class, lexer::next);
    }
}
