import org.example.lexer.LexerImpl;
import org.example.lexer.error.*;
import org.example.parser.Enum.Type;
import org.example.parser.Error.ParsingException;
import org.example.parser.ParserImpl;
import org.example.parser.Structure.Expression.ArthmeticExpression;
import org.example.parser.Structure.Expression.IdentifierExpression;
import org.example.parser.Structure.Expression.Literals.LiteralInteger;
import org.example.parser.Structure.Expression.RelationExpression;
import org.example.parser.Structure.OtherComponents.FunctionDefinition;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.parser.Structure.Statement.*;
import org.example.token.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ParserUnitTests {

    @Test
    public void basicMockTest() throws Exception {
//        "fn int function() {" +
//                "int x = 2;" +
//                "return 2+3;" +
//                "}"
        Position p = new Position(0, 0);
        List<Token> tokens = Arrays.asList(
                new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"function"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"x"),
                new KeywordOrOperandToken(TokenType.EQUAL, p,"="),
                new IntegerToken(p, 2),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.RETURN, p,"return"),
                new IntegerToken(p, 2),
                new KeywordOrOperandToken(TokenType.PLUS, p,"+"),
                new IntegerToken(p, 3),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
                new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
        );

        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 1);
        Assert.assertEquals(functionDefinitionList.containsKey("function"), true);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getFirstOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getSecondOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getPosition().getLine(), 0);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getPosition().getCharacterNumber(), 0);
        Assert.assertEquals(functionDefinitionList.get("function").getName(), "function");
        Assert.assertEquals(functionDefinitionList.get("function").getArguments().size(), 0);
        Assert.assertEquals(functionDefinitionList.get("function").getBody().getPosition().getLine(), 0);
        Assert.assertEquals(functionDefinitionList.get("function").getBody().getPosition().getCharacterNumber(), 0);
        Assert.assertEquals(functionDefinitionList.get("function").getBody().getInstructions().size(), 2);

        DefinitionWithExpressionStatement defOne = (DefinitionWithExpressionStatement) functionDefinitionList.get("function").getBody().getInstructions().get(0);
        String name = defOne.getIdentifierName();
        Type type = defOne.getType().getType();
        Position position = defOne.getPosition();
        LiteralInteger integer = (LiteralInteger) defOne.getExpression();
        int value = (int) integer.getValue();
        Assert.assertEquals(name, "x");
        Assert.assertEquals(type, Type.INT);
        Assert.assertEquals(position.getLine(), 0);
        Assert.assertEquals(position.getCharacterNumber(), 0);
        Assert.assertEquals(value, 2);


        ReturnStatement returnStatement = (ReturnStatement) functionDefinitionList.get("function").getBody().getInstructions().get(1);
        ArthmeticExpression arthmeticExpression = (ArthmeticExpression) returnStatement.getExpression();
        LiteralInteger left = (LiteralInteger) arthmeticExpression.getLeft();
        int leftValue = (int) left.getValue();
        LiteralInteger right = (LiteralInteger) arthmeticExpression.getRight();
        int rightValue = (int) right.getValue();
        Assert.assertEquals(leftValue, 2);
        Assert.assertEquals(rightValue, 3);

    }

    @Test
    public void testAddFunction() throws Exception {
//        fn bool add(int x, int y) {
//            while (x != 5) {
//                int x = 2;
//            }
//        }
        Position p = new Position(0, 0);
        List<Token> tokens = Arrays.asList(
                new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
                new KeywordOrOperandToken(TokenType.BOOL, p, "bool"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p, "add"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.INTEGER, p, "int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p, "x"),
                new KeywordOrOperandToken(TokenType.COMMA, p, ","),
                new KeywordOrOperandToken(TokenType.INTEGER, p, "int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p, "y"),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p, ")"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.WHILE, p, "while"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p, "x"),
                new KeywordOrOperandToken(TokenType.NOTEQUAL, p, "!="),
                new IntegerToken(p, 5),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p, ")"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.INTEGER, p, "int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p, "x"),
                new KeywordOrOperandToken(TokenType.EQUAL, p, "="),
                new IntegerToken(p, 2),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p, ";"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p, "}"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p, "}"),
                new KeywordOrOperandToken(TokenType.END_OF_FILE, p, "")
        );

        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 1);
        Assert.assertEquals(functionDefinitionList.containsKey("add"), true);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getType(), Type.BOOL);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getFirstOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getSecondOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getPosition().getLine(), 0);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getPosition().getCharacterNumber(), 0);
        Assert.assertEquals(functionDefinitionList.get("add").getName(), "add");
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().size(), 2);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(0).getType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(1).getType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(0).getName(), "x");
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(1).getName(), "y");

        Assert.assertEquals(functionDefinitionList.get("add").getBody().getPosition().getLine(), 0);
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getPosition().getCharacterNumber(), 0);
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getInstructions().size(), 1);

        WhileStatement whileStatement= (WhileStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);
        RelationExpression relationExpression = (RelationExpression) whileStatement.getExpression();
        DefinitionWithExpressionStatement assignment = (DefinitionWithExpressionStatement) whileStatement.getBlockStatement().getInstructions().get(0);

        IdentifierExpression identifierExpression = (IdentifierExpression) relationExpression.getLeft();
        LiteralInteger literalInteger = (LiteralInteger) relationExpression.getRight();

        Assert.assertEquals(identifierExpression.getName(), "x");
        Assert.assertEquals(literalInteger.getValue(), 5);

        String name = assignment.getIdentifierName();
        LiteralInteger assignmentInteger = (LiteralInteger) assignment.getExpression();

        Assert.assertEquals(name, "x");
        Assert.assertEquals(assignmentInteger.getValue(), 2);
    }

    @Test
    public void functionDefinitionWithoutBodyThrowsException() throws Exception {
        Position p = new Position(0, 0);
        List<Token> tokens = Arrays.asList(
                new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"function"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
        );

        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        assertThrows(Exception.class, parser::parseProgram);
    }

    @Test
    public void functionDefinitionWithEmptyBody() throws Exception {
        Position p = new Position(0, 0);
        List<Token> tokens = Arrays.asList(
                new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"function"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
                new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
        );

        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 1);
        Assert.assertEquals(functionDefinitionList.containsKey("function"), true);
        Assert.assertEquals(functionDefinitionList.get("function").getBody().getInstructions().size(), 0);
    }

    @Test
    public void getMaxIntReturnsMaxIntegerValue() throws Exception {
//        fn int getMaxInt() {
//            return 2147483647;
//        }


        Position p = new Position(0, 0);
        List<Token> tokens = Arrays.asList(
                new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"getMaxInt"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.RETURN, p,"return"),
                new IntegerToken(p, 2147483647),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
                new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
        );

        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 1);
        Assert.assertEquals(functionDefinitionList.containsKey("getMaxInt"), true);
        Assert.assertEquals(functionDefinitionList.get("getMaxInt").getReturnType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("getMaxInt").getBody().getInstructions().size(), 1);

        ReturnStatement returnStatement = (ReturnStatement) functionDefinitionList.get("getMaxInt").getBody().getInstructions().get(0);
        LiteralInteger returnedValue = (LiteralInteger) returnStatement.getExpression();
        Assert.assertEquals(returnedValue.getValue(), 2147483647);
    }

    @Test
    public void mainFunctionCallsGetMaxInt() throws Exception {
//        fn int main() {
//            int x = getmaxInt();
//            print($String x)
//            return 0;
//        }

        Position p = new Position(0, 0);
        List<Token> tokens = Arrays.asList(
                new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"main"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"x"),
                new KeywordOrOperandToken(TokenType.EQUAL, p,"="),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"getMaxInt"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"print"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p,"("),
                new KeywordOrOperandToken(TokenType.CAST, p,"$"),
                new KeywordOrOperandToken(TokenType.STRING, p,"String"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"x"),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.RETURN, p,"return"),
                new IntegerToken(p, 0),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
                new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
        );

        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 1);
        Assert.assertEquals(functionDefinitionList.containsKey("main"), true);
        Assert.assertEquals(functionDefinitionList.get("main").getReturnType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("main").getBody().getInstructions().size(), 3);

        DefinitionWithExpressionStatement definitionStatement = (DefinitionWithExpressionStatement) functionDefinitionList.get("main").getBody().getInstructions().get(0);
        Assert.assertEquals(definitionStatement.getIdentifierName(), "x");
        Assert.assertEquals(definitionStatement.getType().getType(), Type.INT);

        FunctionCall functionCall = (FunctionCall) definitionStatement.getExpression();
        Assert.assertEquals(functionCall.getName(), "getMaxInt");

        ReturnStatement returnStatement = (ReturnStatement) functionDefinitionList.get("main").getBody().getInstructions().get(2);
        LiteralInteger returnedValue = (LiteralInteger) returnStatement.getExpression();
        Assert.assertEquals(returnedValue.getValue(), 0);
    }

    @Test
    public void EmptyFunctionsMap() throws Exception {
        Position p = new Position(0, 0);
        List<Token> tokens = Arrays.asList(
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"x"),
                new KeywordOrOperandToken(TokenType.EQUAL, p,"="),
                new IntegerToken(p, 2),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
        );

        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        Program program = parser.parseProgram();
        Assert.assertEquals(program.getFunctionDefinitions().size(), 0);

    }

    @Test
    public void printIfEvenFunctionPrintsCorrectMessage() throws Exception {
//        fn void printIfEven(int number) {
//            if (number > 0) {
//                print("The number is bigger than 0.");
//            } else {
//                print("The number is less than 0.");
//            }
//        }

        Position p = new Position(0, 0);
        List<Token> tokens = Arrays.asList(
                new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
                new KeywordOrOperandToken(TokenType.VOID, p,"void"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"printIfEven"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"number"),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.IF, p,"if"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"number"),
                new KeywordOrOperandToken(TokenType.GREATER, p,">"),
                new IntegerToken(p, 0),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"print"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new StringToken(p, "The number is bigger than 0."),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
                new KeywordOrOperandToken(TokenType.ELSE, p,"else"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
                new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"print"),
                new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
                new StringToken(p, "The number is less than 0."),
                new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
                new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
                new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
                new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
        );

        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 1);
        Assert.assertEquals(functionDefinitionList.containsKey("printIfEven"), true);
        Assert.assertEquals(functionDefinitionList.get("printIfEven").getReturnType(), null);
        Assert.assertEquals(functionDefinitionList.get("printIfEven").getBody().getInstructions().size(), 1);
    }

    @Test
    public void addFunctionIteratesOverArray() throws Exception {
//        fn add(int x) {
//            for {int x : a} {
//                int x = 2;
//            }
//        }
    Position p = new Position(0, 0);
    List<Token> tokens = Arrays.asList(
            new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
            new KeywordOrOperandToken(TokenType.BOOL, p,"bool"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"add"),
            new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
            new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"x"),
            new KeywordOrOperandToken(TokenType.COMMA, p,","),
            new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"y"),
            new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
            new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
            new KeywordOrOperandToken(TokenType.FOR, p,"for"),
            new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
            new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"x"),
            new KeywordOrOperandToken(TokenType.COLON, p,":"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"a"),
            new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
            new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
            new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"x"),
            new KeywordOrOperandToken(TokenType.EQUAL, p,"="),
            new IntegerToken(p, 2),
            new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
            new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
            new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
            new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
    );
        LexerImpl mockLexer = mock(LexerImpl.class);
        Iterator<Token> tokenIterator = tokens.iterator();
        when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

        ParserImpl parser = new ParserImpl(mockLexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }

    @Test
    public void functionDefinitionWithMissingClosingBraceThrowsException() throws Exception {
    // "fn int function() {"
    Position p = new Position(0, 0);
    List<Token> tokens = Arrays.asList(
            new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
            new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"function"),
            new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
            new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
            new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
            new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
    );

    LexerImpl mockLexer = mock(LexerImpl.class);
    Iterator<Token> tokenIterator = tokens.iterator();
    when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

    ParserImpl parser = new ParserImpl(mockLexer);
    assertThrows(Exception.class, parser::parseProgram);
    }

    @Test
    public void mainFunctionInitializesList() throws Exception {
    // Code snippet:
    // "fn int main() {" +
    // "List<int> list = [1, 2];" +
    // "}"
    Position p = new Position(0, 0);
    List<Token> tokens = Arrays.asList(
            new KeywordOrOperandToken(TokenType.FUNCTION, p, "fn"),
            new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"main"),
            new KeywordOrOperandToken(TokenType.BRACKET_OPEN, p, "("),
            new KeywordOrOperandToken(TokenType.BRACKET_CLOSE, p,")"),
            new KeywordOrOperandToken(TokenType.CURLY_BRACKET_OPEN, p, "{"),
            new KeywordOrOperandToken(TokenType.LIST, p,"List"),
            new KeywordOrOperandToken(TokenType.LESS, p,"<"),
            new KeywordOrOperandToken(TokenType.INTEGER, p,"int"),
            new KeywordOrOperandToken(TokenType.GREATER, p,">"),
            new KeywordOrOperandToken(TokenType.IDENTIFIER, p,"list"),
            new KeywordOrOperandToken(TokenType.EQUAL, p,"="),
            new KeywordOrOperandToken(TokenType.SQUARE_BRACKET_OPEN, p,"["),
            new IntegerToken(p, 1),
            new KeywordOrOperandToken(TokenType.COMMA, p,","),
            new IntegerToken(p, 2),
            new KeywordOrOperandToken(TokenType.SQUARE_BRACKET_CLOSE, p,"]"),
            new KeywordOrOperandToken(TokenType.SEMICOLON, p,";"),
            new KeywordOrOperandToken(TokenType.CURLY_BRACKET_CLOSE, p,"}"),
            new KeywordOrOperandToken(TokenType.END_OF_FILE, p,"")
    );

    LexerImpl mockLexer = mock(LexerImpl.class);
    Iterator<Token> tokenIterator = tokens.iterator();
    when(mockLexer.next()).thenAnswer(invocation -> tokenIterator.hasNext() ? tokenIterator.next() : null);

    ParserImpl parser = new ParserImpl(mockLexer);
    Program program = parser.parseProgram();
    Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
    Assert.assertEquals(functionDefinitionList.size(), 1);
    Assert.assertEquals(functionDefinitionList.containsKey("main"), true);
    Assert.assertEquals(functionDefinitionList.get("main").getReturnType().getType(), Type.INT);
    Assert.assertEquals(functionDefinitionList.get("main").getBody().getInstructions().size(), 1);

    DefinitionWithExpressionStatement definitionStatement = (DefinitionWithExpressionStatement) functionDefinitionList.get("main").getBody().getInstructions().get(0);
    Assert.assertEquals(definitionStatement.getIdentifierName(), "list");
    Assert.assertEquals(definitionStatement.getType().getType(), Type.LIST);
}
}
