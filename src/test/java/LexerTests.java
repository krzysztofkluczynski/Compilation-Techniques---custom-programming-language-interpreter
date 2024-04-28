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

    private void assertToken(Token token, TokenType expectedType, int expectedLine, int expectedStartPos) {
        Assert.assertEquals(expectedType, token.getType());
        Assert.assertEquals(expectedLine, token.getPosition().getLine());
        Assert.assertEquals(expectedStartPos, token.getPosition().getCharacterNumber());
    }

    private void assertToken(Token token, TokenType expectedType, int expectedLine, int expectedStartChar, Object expectedValue) {
        assertToken(token, expectedType, expectedLine, expectedStartChar);
        Assert.assertEquals(expectedValue, token.getValue());
    }

    @Test
    public void testLexerTokenTypesAndPositions() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                   int a = 3; float b = 3.14; bool c = true; String x = "ala ma kota123";
                   """
        );
        LexerImpl lexer = new LexerImpl(reader);

        assertToken(lexer.next(), TokenType.INTEGER, 1, 0);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 4);
        assertToken(lexer.next(), TokenType.EQUAL, 1, 6);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 1, 8, 3);
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 9);

        assertToken(lexer.next(), TokenType.FLOAT, 1, 11);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 17);
        assertToken(lexer.next(), TokenType.EQUAL, 1, 19);
        assertToken(lexer.next(), TokenType.FLOAT_LITERAL, 1, 21, 3.14f);
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 25);

        assertToken(lexer.next(), TokenType.BOOL, 1, 27);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 32);
        assertToken(lexer.next(), TokenType.EQUAL, 1, 34);
        assertToken(lexer.next(), TokenType.BOOL_LITERAL, 1, 36, true);
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 40);

        assertToken(lexer.next(), TokenType.STRING, 1, 42);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 49);
        assertToken(lexer.next(), TokenType.EQUAL, 1, 51);
        assertToken(lexer.next(), TokenType.STRING_LITERAL, 1, 53, "ala ma kota123");
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 69);
    }


    @Test
    public void testFullFeaturesIncludingCommentsAndPositions() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, NumberMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                
                Dictionary<String, int> var_dict = |
                            "dog": 3,
                    "cat": 4,
                            "cow": 5,
                                            "hamster": 6 
                 |;
                \n
                var_tuple.get(0) // Getting the first item from the tuple
                var_tuple.set(0, 2) // Setting the value at index 0 to 2
                var_dict.sort(( Tuple<String, int> a, Tuple<String, int> b) => a.get(1) > b.get(1)); // if value a is greater than value b, then a should be first in order
                """
        );
        LexerImpl lexer = new LexerImpl(reader);

        // Test Dictionary with type annotations
        assertToken(lexer.next(), TokenType.DICTIONARY, 2, 0);
        assertToken(lexer.next(), TokenType.LESS, 2, 10);
        assertToken(lexer.next(), TokenType.STRING, 2, 11);
        assertToken(lexer.next(), TokenType.COMMA, 2, 17);
        assertToken(lexer.next(), TokenType.INTEGER, 2, 19);
        assertToken(lexer.next(), TokenType.GREATER, 2, 22);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 2, 24);
        assertToken(lexer.next(), TokenType.EQUAL, 2, 33);
        assertToken(lexer.next(), TokenType.PIPE, 2, 35);

        // Test dictionary contents
        assertToken(lexer.next(), TokenType.STRING_LITERAL, 3, 12);
        assertToken(lexer.next(), TokenType.COLON, 3, 17);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 3, 19);
        assertToken(lexer.next(), TokenType.COMMA, 3, 20);

        assertToken(lexer.next(), TokenType.STRING_LITERAL, 4, 4);
        assertToken(lexer.next(), TokenType.COLON, 4, 9);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 4, 11);
        assertToken(lexer.next(), TokenType.COMMA, 4, 12);

        assertToken(lexer.next(), TokenType.STRING_LITERAL, 5, 12);
        assertToken(lexer.next(), TokenType.COLON, 5, 17);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 5, 19);
        assertToken(lexer.next(), TokenType.COMMA, 5, 20);

        assertToken(lexer.next(), TokenType.STRING_LITERAL, 6, 28);
        assertToken(lexer.next(), TokenType.COLON, 6, 37);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 6, 39);

        assertToken(lexer.next(), TokenType.PIPE, 7, 1);
        assertToken(lexer.next(), TokenType.SEMICOLON, 7, 2);

        // Test tuple method get() and comments
        assertToken(lexer.next(), TokenType.IDENTIFIER, 9, 0);
        assertToken(lexer.next(), TokenType.DOT, 9, 9);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 9, 10);
        assertToken(lexer.next(), TokenType.BRACKET_OPEN, 9, 13);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 9, 14);
        assertToken(lexer.next(), TokenType.BRACKET_CLOSE, 9, 15);
        assertToken(lexer.next(), TokenType.ONE_LINE_COMMENT, 9, 17);

        // Test tuple method set() and comments
        assertToken(lexer.next(), TokenType.IDENTIFIER, 10, 0);
        assertToken(lexer.next(), TokenType.DOT, 10, 9);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 10, 10);
        assertToken(lexer.next(), TokenType.BRACKET_OPEN, 10, 13);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 10, 14);
        assertToken(lexer.next(), TokenType.COMMA, 10, 15);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 10, 17);
        assertToken(lexer.next(), TokenType.BRACKET_CLOSE, 10, 18);
        assertToken(lexer.next(), TokenType.ONE_LINE_COMMENT, 10, 20);

        // Test lambda expression in sort function
        assertToken(lexer.next(), TokenType.IDENTIFIER, 11, 0);
        assertToken(lexer.next(), TokenType.DOT, 11, 8);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 11, 9);
        assertToken(lexer.next(), TokenType.BRACKET_OPEN, 11, 13);
        assertToken(lexer.next(), TokenType.BRACKET_OPEN, 11, 14);
        assertToken(lexer.next(), TokenType.TUPLE, 11, 16);
        assertToken(lexer.next(), TokenType.LESS, 11, 21);
        assertToken(lexer.next(), TokenType.STRING, 11, 22);
        assertToken(lexer.next(), TokenType.COMMA, 11, 28);
        assertToken(lexer.next(), TokenType.INTEGER, 11, 30);
        assertToken(lexer.next(), TokenType.GREATER, 11, 33);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 11, 35);
        assertToken(lexer.next(), TokenType.COMMA, 11, 36);
        assertToken(lexer.next(), TokenType.TUPLE, 11, 38);
        assertToken(lexer.next(), TokenType.LESS, 11, 43);
        assertToken(lexer.next(), TokenType.STRING, 11, 44);
        assertToken(lexer.next(), TokenType.COMMA, 11, 50);
        assertToken(lexer.next(), TokenType.INTEGER, 11, 52);
        assertToken(lexer.next(), TokenType.GREATER, 11, 55);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 11, 57);
        assertToken(lexer.next(), TokenType.BRACKET_CLOSE, 11, 58);
        assertToken(lexer.next(), TokenType.LAMBDA, 11, 60);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 11, 63);
        assertToken(lexer.next(), TokenType.DOT, 11, 64);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 11, 65);
        assertToken(lexer.next(), TokenType.BRACKET_OPEN, 11, 68);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 11, 69);
        assertToken(lexer.next(), TokenType.BRACKET_CLOSE, 11, 70);
        assertToken(lexer.next(), TokenType.GREATER, 11, 72);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 11, 74);
        assertToken(lexer.next(), TokenType.DOT, 11, 75);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 11, 76);
        assertToken(lexer.next(), TokenType.BRACKET_OPEN, 11, 79);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 11, 80);
        assertToken(lexer.next(), TokenType.BRACKET_CLOSE, 11, 81);
        assertToken(lexer.next(), TokenType.BRACKET_CLOSE, 11, 82);
        assertToken(lexer.next(), TokenType.SEMICOLON, 11, 83);
        assertToken(lexer.next(), TokenType.ONE_LINE_COMMENT, 11, 85);
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
