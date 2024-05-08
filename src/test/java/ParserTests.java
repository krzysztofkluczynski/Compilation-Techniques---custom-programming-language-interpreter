import org.example.lexer.Lexer;
import org.example.lexer.LexerImpl;
import org.example.lexer.error.*;
import org.example.parser.Enum.MultiplicativeType;
import org.example.parser.Enum.RelativeType;
import org.example.parser.Enum.Type;
import org.example.parser.ParserImpl;
import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.LiteralBool;
import org.example.parser.Structure.Expression.Literals.LiteralInteger;
import org.example.parser.Structure.Expression.Literals.LiteralString;
import org.example.parser.Structure.Expression.Literals.LiteralTuple;
import org.example.parser.Structure.OtherComponents.FunctionDefinition;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.parser.Structure.OtherComponents.TypeDeclaration;
import org.example.parser.Structure.Statement.*;
import org.example.reader.DataStreamInputReader;
import org.example.token.IntegerToken;
import org.example.token.Position;
import org.example.token.Token;
import org.example.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.beans.Expression;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ParserTests {

    @Test
    public void testSimpleIntFunction() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                "fn int function() {" +
                    "int x = 2;" +
                    "return 2+3;" +
                        "}"
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 1);
        Assert.assertEquals(functionDefinitionList.containsKey("function"), true);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getFirstOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getSecondOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getPosition().getLine(), 1);
        Assert.assertEquals(functionDefinitionList.get("function").getReturnType().getPosition().getCharacterNumber(), 3);
        Assert.assertEquals(functionDefinitionList.get("function").getName(), "function");
        Assert.assertEquals(functionDefinitionList.get("function").getArguments().size(), 0);
        Assert.assertEquals(functionDefinitionList.get("function").getBody().getPosition().getLine(), 1);
        Assert.assertEquals(functionDefinitionList.get("function").getBody().getPosition().getCharacterNumber(), 18);
        Assert.assertEquals(functionDefinitionList.get("function").getBody().getInstructions().size(), 2);

        DefinitionWithExpressionStatement defOne = (DefinitionWithExpressionStatement) functionDefinitionList.get("function").getBody().getInstructions().get(0);
        String name = defOne.getIdentifierName();
        Type type = defOne.getType().getType();
        Position position = defOne.getPosition();
        LiteralInteger integer = (LiteralInteger) defOne.getExpression();
        int value = integer.getValue();
        Assert.assertEquals(name, "x");
        Assert.assertEquals(type, Type.INT);
        Assert.assertEquals(position.getLine(), 1);
        Assert.assertEquals(position.getCharacterNumber(), 19);
        Assert.assertEquals(value, 2);


        ReturnStatement returnStatement = (ReturnStatement) functionDefinitionList.get("function").getBody().getInstructions().get(1);
        ArthmeticExpression arthmeticExpression = (ArthmeticExpression) returnStatement.getExpression();
        LiteralInteger left = (LiteralInteger) arthmeticExpression.getLeft();
        int leftValue = left.getValue();
        LiteralInteger right = (LiteralInteger) arthmeticExpression.getRight();
        int rightValue = right.getValue();
        Assert.assertEquals(leftValue, 2);
        Assert.assertEquals(rightValue, 3);

    }

    @Test
    public void testSimpleboolFunction() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn bool add(int x, int y) {
                            int z;
                            z = 3*5;
                            return true;
                         }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 1);
        Assert.assertEquals(functionDefinitionList.containsKey("add"), true);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getType(), Type.BOOL);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getFirstOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getSecondOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getPosition().getLine(), 1);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getPosition().getCharacterNumber(), 3);
        Assert.assertEquals(functionDefinitionList.get("add").getName(), "add");
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().size(), 2);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(0).getType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(1).getType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(0).getName(), "x");
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(1).getName(), "y");

        Assert.assertEquals(functionDefinitionList.get("add").getBody().getPosition().getLine(), 1);
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getPosition().getCharacterNumber(), 26);
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getInstructions().size(), 3);

        DeclarationStatement declarationStatement= (DeclarationStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);

        AssignmentWithExpressionStatement assignment = (AssignmentWithExpressionStatement) functionDefinitionList.get("add").getBody().getInstructions().get(1);

        ReturnStatement returnStatement = (ReturnStatement) functionDefinitionList.get("add").getBody().getInstructions().get(2);

        String declarationName = declarationStatement.getName();
        Type declarationType = declarationStatement.getType().getType();

        String identiferName = assignment.getIdentifierName();
        MultiplicativeExpression multiplicativeExpression = (MultiplicativeExpression) assignment.getExpression();
        LiteralInteger literalIntegerleft = (LiteralInteger) multiplicativeExpression.getLeft();
        LiteralInteger literalIntegerright = (LiteralInteger) multiplicativeExpression.getRight();


        LiteralBool literalBool = (LiteralBool) returnStatement.getExpression();

        Assert.assertEquals(declarationName, "z");
        Assert.assertEquals(declarationType, Type.INT);

        Assert.assertEquals(identiferName, "z");
        Assert.assertEquals(literalIntegerleft.getValue(), 3);
        Assert.assertEquals(literalIntegerright.getValue(), 5);

        Assert.assertEquals(literalBool.getValue(), true);
    }

    @Test
    public void testDoubleFunctionComplexTypeFunction() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                 fn List<int> add(String x, float y) {
                    bool zmienna = 4 != 3 and 3 > 2;
                    return "String";
                  }
                
                  fn Dictionary<float, float> train(int x, int y) {
                    Tuple<int, float> pair;
                    Tuple<int, float> pair = (3, 4);
                    return true;
                  }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();
        Assert.assertEquals(functionDefinitionList.size(), 2);
        Assert.assertEquals(functionDefinitionList.containsKey("add"), true);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getType(), Type.LIST);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getFirstOptionalParam(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getSecondOptionalParam(), null);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getPosition().getLine(), 1);
        Assert.assertEquals(functionDefinitionList.get("add").getReturnType().getPosition().getCharacterNumber(), 3);
        Assert.assertEquals(functionDefinitionList.get("add").getName(), "add");
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().size(), 2);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(0).getType().getType(), Type.STRING);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(1).getType().getType(), Type.FLOAT);
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(0).getName(), "x");
        Assert.assertEquals(functionDefinitionList.get("add").getArguments().get(1).getName(), "y");

        Assert.assertEquals(functionDefinitionList.get("add").getBody().getPosition().getLine(), 1);
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getInstructions().size(), 2);

        DefinitionWithExpressionStatement definition = (DefinitionWithExpressionStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);

        ReturnStatement returnStatement = (ReturnStatement) functionDefinitionList.get("add").getBody().getInstructions().get(1);

        String name = definition.getIdentifierName();
        Type declarationType = definition.getType().getType();

        AndExpression andExpression = (AndExpression) definition.getExpression();
        RelationExpression relationExpressionLeft = (RelationExpression) andExpression.getLeft();
        RelationExpression relationExpressionRight= (RelationExpression) andExpression.getRight();

        RelativeType relativeTypeLeft = relationExpressionLeft.getRelativeOperand();
        LiteralInteger leftLeftInteger = (LiteralInteger) relationExpressionLeft.getLeft();
        LiteralInteger leftRightInteger = (LiteralInteger) relationExpressionLeft.getRight();

        RelativeType relativeTypeRight = relationExpressionRight.getRelativeOperand();
        LiteralInteger rightLeftInteger = (LiteralInteger) relationExpressionRight.getLeft();
        LiteralInteger rightRightInteger = (LiteralInteger) relationExpressionRight.getRight();

        Position returnPosition = returnStatement.getPosition();
        LiteralString literalString= (LiteralString) returnStatement.getExpression();

        Assert.assertEquals(name, "zmienna");
        Assert.assertEquals(declarationType, Type.BOOL);

        Assert.assertEquals(relativeTypeLeft, RelativeType.NOT_EQUAL);
        Assert.assertEquals(relativeTypeRight, RelativeType.MORE);

        Assert.assertEquals(leftLeftInteger.getValue(), 4);
        Assert.assertEquals(leftRightInteger.getValue(), 3);
        Assert.assertEquals(rightLeftInteger.getValue(), 3);
        Assert.assertEquals(rightRightInteger.getValue(), 2);


        Assert.assertEquals(returnPosition, new Position(3, 3));
        Assert.assertEquals(literalString.getValue(), "String");



        Assert.assertEquals(functionDefinitionList.containsKey("train"), true);
        Assert.assertEquals(functionDefinitionList.get("train").getReturnType().getType(), Type.DICTIONARY);
        Assert.assertEquals(functionDefinitionList.get("train").getReturnType().getFirstOptionalParam(), Type.FLOAT);
        Assert.assertEquals(functionDefinitionList.get("train").getReturnType().getSecondOptionalParam(), Type.FLOAT);
        Assert.assertEquals(functionDefinitionList.get("train").getReturnType().getPosition().getLine(), 5); //java strings skips white characters as newline
        //Assert.assertEquals(functionDefinitionList.get("train").getReturnType().getPosition().getCharacterNumber(), 1);
        Assert.assertEquals(functionDefinitionList.get("train").getName(), "train");
        Assert.assertEquals(functionDefinitionList.get("train").getArguments().size(), 2);
        Assert.assertEquals(functionDefinitionList.get("train").getArguments().get(0).getType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("train").getArguments().get(1).getType().getType(), Type.INT);
        Assert.assertEquals(functionDefinitionList.get("train").getArguments().get(0).getName(), "x");
        Assert.assertEquals(functionDefinitionList.get("train").getArguments().get(1).getName(), "y");

//        TypeDeclaration declarationStatement = (TypeDeclaration) functionDefinitionList.get("train").getReturnType();
//        Type secondDeclarationType = declarationStatement.getType();
//        Type firstOptionalType = declarationStatement.getFirstOptionalParam();
//        Type secondOptionalType = declarationStatement.getSecondOptionalParam();
//
//        Assert.assertEquals(secondDeclarationType, Type.DICTIONARY);
//        Assert.assertEquals(firstOptionalType, Type.INT);
//        Assert.assertEquals(secondDeclarationType, Type.INT);

        DeclarationStatement tupleDeclaration = (DeclarationStatement) functionDefinitionList.get("train").getBody().getInstructions().get(0);
        Assert.assertEquals(tupleDeclaration.getType().getType(), Type.TUPLE);
        Assert.assertEquals(tupleDeclaration.getType().getFirstOptionalParam(), Type.INT);
        Assert.assertEquals(tupleDeclaration.getType().getSecondOptionalParam(), Type.FLOAT);
        Assert.assertEquals(tupleDeclaration.getName(), "pair");

        DefinitionWithExpressionStatement definitionWithExpressionStatement = (DefinitionWithExpressionStatement) functionDefinitionList.get("train").getBody().getInstructions().get(1);
        Assert.assertEquals(definitionWithExpressionStatement.getType().getType(), Type.TUPLE);
        Assert.assertEquals(definitionWithExpressionStatement.getType().getFirstOptionalParam(), Type.INT);
        Assert.assertEquals(definitionWithExpressionStatement.getType().getSecondOptionalParam(), Type.FLOAT);
        Assert.assertEquals(tupleDeclaration.getName(), "pair");
        LiteralTuple literalTuple = (LiteralTuple) definitionWithExpressionStatement.getExpression();
        LiteralInteger literalInteger = (LiteralInteger) literalTuple.getObjectOne();
        LiteralInteger literalInteger1 = (LiteralInteger) literalTuple.getObjectTwo();

        Assert.assertEquals(literalInteger.getValue(), 3);
        Assert.assertEquals(literalInteger1.getValue(), 4);

        ReturnStatement returnStatement1 = (ReturnStatement) functionDefinitionList.get("train").getBody().getInstructions().get(2);

        LiteralBool returnValue = (LiteralBool) returnStatement1.getExpression();
        Assert.assertEquals(returnValue.getValue(), true);
    }



}
