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

    // Arithmetic Operators Tests
    @Test
    public void testPlusToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("+");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.PLUS, 1, 0, "+");
    }

    @Test
    public void testMinusToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("-");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.MINUS, 1, 0, "-");
    }

    @Test
    public void testMultiplyToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("*");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.MULTIPLY, 1, 0, "*");
    }

    @Test
    public void testDivideToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("/");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.DIVIDE, 1, 0, "/");
    }

    @Test
    public void testEqualToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("=");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.EQUAL, 1, 0, "=");
    }

    // Comparison Operators Tests
    @Test
    public void testEqualCompToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("==");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.EQUAL_COMP, 1, 0, "==");
    }

    @Test
    public void testNotEqualToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("!=");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.NOTEQUAL, 1, 0, "!=");
    }

    @Test
    public void testGreaterToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(">");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.GREATER, 1, 0, ">");
    }

    @Test
    public void testLessToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("<");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.LESS, 1, 0, "<");
    }

    @Test
    public void testLessEqualToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("<=");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.LESSEQUAL, 1, 0, "<=");
    }

    @Test
    public void testGreaterEqualToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(">=");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.GREATEREQUAL, 1, 0, ">=");
    }

    @Test
    public void testAndToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("and");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.AND, 1, 0, "and");
    }

    @Test
    public void testOrToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("or");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.OR, 1, 0, "or");
    }

    @Test
    public void testNotToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("not");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.NOT, 1, 0, "not");
    }

    // Types Tests
    @Test
    public void testIntegerToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("int");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.INTEGER, 1, 0, "int");
    }

    @Test
    public void testFloatToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("float");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.FLOAT, 1, 0, "float");
    }

    @Test
    public void testBoolToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("bool");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.BOOL, 1, 0, "bool");
    }

    @Test
    public void testStringToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("String");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.STRING, 1, 0, "String");
    }

    @Test
    public void testDictionaryToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("Dictionary");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.DICTIONARY, 1, 0, "Dictionary");
    }

    @Test
    public void testListToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("List");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.LIST, 1, 0, "List");
    }

    @Test
    public void testTupleToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("Tuple");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.TUPLE, 1, 0, "Tuple");
    }

    @Test
    public void testVoidToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("void");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.VOID, 1, 0, "void");
    }

    @Test
    public void testCurlyBracketCloseToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("}");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.CURLY_BRACKET_CLOSE, 1, 0, "}");
    }

    @Test
    public void testBracketOpenToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("(");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.BRACKET_OPEN, 1, 0, "(");
    }

    @Test
    public void testBracketCloseToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(")");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.BRACKET_CLOSE, 1, 0, ")");
    }

    @Test
    public void testSquareBracketOpenToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("[");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.SQUARE_BRACKET_OPEN, 1, 0, "[");
    }

    @Test
    public void testSquareBracketCloseToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("]");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.SQUARE_BRACKET_CLOSE, 1, 0, "]");
    }

    @Test
    public void testPipeToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("|");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.PIPE, 1, 0, "|");
    }

    @Test
    public void testWhileToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("while");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.WHILE, 1, 0, "while");
    }

    @Test
    public void testForToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("for");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.FOR, 1, 0, "for");
    }

    @Test
    public void testIfToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("if");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.IF, 1, 0, "if");
    }

    @Test
    public void testElifToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("elif");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.ELIF, 1, 0, "elif");
    }

    @Test
    public void testElseToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("else");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.ELSE, 1, 0, "else");
    }

    @Test
    public void testSelectToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("SELECT");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.SELECT, 1, 0, "SELECT");
    }

    @Test
    public void testFromToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("FROM");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.FROM, 1, 0, "FROM");
    }

    @Test
    public void testWhereToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("WHERE");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.WHERE, 1, 0, "WHERE");
    }

    @Test
    public void testOrderByToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("ORDER_BY");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.ORDER_BY, 1, 0, "ORDER_BY");
    }

    @Test
    public void testAscendingToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("ASC");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.ASCENDING, 1, 0, "ASC");
    }

    @Test
    public void testDescendingToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("DSC");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.DESCENDING, 1, 0, "DSC");
    }

    @Test
    public void testFunctionToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("fn");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.FUNCTION, 1, 0, "fn");
    }

    @Test
    public void testReturnToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("return");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.RETURN, 1, 0, "return");
    }

    @Test
    public void testCastToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("$");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.CAST, 1, 0, "$");
    }

    @Test
    public void testLambdaToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("=>");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.LAMBDA, 1, 0, "=>");
    }

    @Test
    public void testSemicolonToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(";");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 0, ";");
    }

    @Test
    public void testColonToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(":");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.COLON, 1, 0, ":");
    }

    @Test
    public void testCommaToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(",");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.COMMA, 1, 0, ",");
    }

    @Test
    public void testDotToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(".");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.DOT, 1, 0, ".");
    }

    @Test
    public void testOneLineCommentToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("//This is a comment");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.ONE_LINE_COMMENT, 1, 0, "This is a comment");
    }

    @Test
    public void testMultiLineCommentToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("/* Multi-line\ncomment */");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.MULTI_LINE_COMMENT, 1, 0, " Multi-line\ncomment ");
    }

    @Test
    public void testIdentifierToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("variableName");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 0, "variableName");
    }

    @Test
    public void testBoolLiteralToken() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("true");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.BOOL_LITERAL, 1, 0, true);
    }

    @Test
    public void testBoolLiteralTokenFalse() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("false");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.BOOL_LITERAL, 1, 0, false);
    }

    @Test
    public void testStringLiteral() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("\"string test\"");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.STRING_LITERAL, 1, 0, "string test");
    }

    @Test
    public void testIntLiteral() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("123456");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.INT_LITERAL, 1, 0, 123456);
    }


    @Test
    public void testFloatLiteral() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader("3.14532");
        LexerImpl lexer = new LexerImpl(reader);
        assertToken(lexer.next(), TokenType.FLOAT_LITERAL, 1, 0, 3.14532f);
    }







    @Test
    public void testLexerTokenTypesAndPositions() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IntMaxValueExceededException, DecimalMaxValueExceededException, IdentifierTooLongException {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                   int a = 31234; float b = 3.14; bool c = true; String x = "ala ma kota123";
                   """
        );
        LexerImpl lexer = new LexerImpl(reader);

        assertToken(lexer.next(), TokenType.INTEGER, 1, 0, "int");
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 4, "a");
        assertToken(lexer.next(), TokenType.EQUAL, 1, 6, "=");
        assertToken(lexer.next(), TokenType.INT_LITERAL, 1, 8, 31234);
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 13, ";");

        assertToken(lexer.next(), TokenType.FLOAT, 1, 15, "float");
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 21, "b");
        assertToken(lexer.next(), TokenType.EQUAL, 1, 23, "=");
        assertToken(lexer.next(), TokenType.FLOAT_LITERAL, 1, 25, 3.14f);
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 29, ";");

        assertToken(lexer.next(), TokenType.BOOL, 1, 31, "bool");
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 36, "c");
        assertToken(lexer.next(), TokenType.EQUAL, 1, 38, "=");
        assertToken(lexer.next(), TokenType.BOOL_LITERAL, 1, 40, true);
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 44, ";");

        assertToken(lexer.next(), TokenType.STRING, 1, 46, "String");
        assertToken(lexer.next(), TokenType.IDENTIFIER, 1, 53, "x");
        assertToken(lexer.next(), TokenType.EQUAL, 1, 55, "=");
        assertToken(lexer.next(), TokenType.STRING_LITERAL, 1, 57, "ala ma kota123");
        assertToken(lexer.next(), TokenType.SEMICOLON, 1, 73, ";");
    }


    @Test
    public void testFullFeaturesIncludingCommentsAndPositions() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IntMaxValueExceededException, DecimalMaxValueExceededException, IdentifierTooLongException {
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
    public void testTuplesAndComments() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IdentifierTooLongException, IntMaxValueExceededException, DecimalMaxValueExceededException {
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
    public void testListOperations() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IdentifierTooLongException, IntMaxValueExceededException, DecimalMaxValueExceededException {
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
    public void testFunctionAndOperations() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IdentifierTooLongException, IntMaxValueExceededException, DecimalMaxValueExceededException {
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
        lexer.next();
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
    public void testSQLLikeQueryParsing() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IdentifierTooLongException, IntMaxValueExceededException, DecimalMaxValueExceededException {
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
    public void testConditionalStructures() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IdentifierTooLongException, IntMaxValueExceededException, DecimalMaxValueExceededException {
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
    public void testControlFlowAndListOperations() throws IOException, ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IdentifierTooLongException, IntMaxValueExceededException, DecimalMaxValueExceededException {
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
        Assertions.assertThrows(IntMaxValueExceededException.class, lexer::next);
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
