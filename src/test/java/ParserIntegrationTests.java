import org.example.lexer.LexerImpl;
import org.example.parser.Enum.AscOrDESC;
import org.example.parser.Enum.RelativeType;
import org.example.parser.Enum.Type;
import org.example.parser.Error.DuplicateIdentiferException;
import org.example.parser.Error.ParsingException;
import org.example.parser.ParserImpl;
import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.*;
import org.example.parser.Structure.OtherComponents.*;
import org.example.parser.Structure.Statement.*;
import org.example.reader.DataStreamInputReader;
import org.example.token.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParserIntegrationTests {

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

    @Test
    public void testWhile() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                    fn bool add(int x, int y) {
                        while (x != 5) {
                            int x = 2;
                        }
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
    public void testIf() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                    fn bool add(int x, int y) {
                        if (x != 5) {
                            int x = 2;
                        }
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
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getInstructions().size(), 1);

        ConditionalStatement conditionalStatement= (ConditionalStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);
        RelationExpression relationExpression = (RelationExpression) conditionalStatement.getIfs().get(0).getExpression();
        DefinitionWithExpressionStatement assignment = (DefinitionWithExpressionStatement) conditionalStatement.getIfs().get(0).getBlockStatement().getInstructions().get(0);

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
    public void testIfElifElse() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                    fn bool add(int x, int y) {
                     if (x != 5) {
                        int x = 3;
                     } elif (true) {
                        int x = 3;
                     } else {
                        int x = 3;
                        }
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
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getInstructions().size(), 1);

        ConditionalStatement conditionalStatement= (ConditionalStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);
        RelationExpression relationExpression = (RelationExpression) conditionalStatement.getIfs().get(0).getExpression();
        DefinitionWithExpressionStatement assignment = (DefinitionWithExpressionStatement) conditionalStatement.getIfs().get(0).getBlockStatement().getInstructions().get(0);

        IdentifierExpression identifierExpression = (IdentifierExpression) relationExpression.getLeft();
        LiteralInteger literalInteger = (LiteralInteger) relationExpression.getRight();

        Assert.assertEquals(identifierExpression.getName(), "x");
        Assert.assertEquals(literalInteger.getValue(), 5);

        String name = assignment.getIdentifierName();
        LiteralInteger assignmentInteger = (LiteralInteger) assignment.getExpression();

        Assert.assertEquals(name, "x");
        Assert.assertEquals(assignmentInteger.getValue(), 3);

        LiteralBool literalBool = (LiteralBool) conditionalStatement.getIfs().get(1).getExpression();
        DefinitionWithExpressionStatement assignment1 = (DefinitionWithExpressionStatement) conditionalStatement.getIfs().get(1).getBlockStatement().getInstructions().get(0);


        Assert.assertEquals(literalBool.getValue() ,true);

        String name1 = assignment1.getIdentifierName();
        LiteralInteger assignmentInteger1 = (LiteralInteger) assignment1.getExpression();

        Assert.assertEquals(name1, "x");
        Assert.assertEquals(assignmentInteger1.getValue(), 3);

        RelationExpression relationExpression2 = (RelationExpression) conditionalStatement.getIfs().get(2).getExpression();
        DefinitionWithExpressionStatement assignment2 = (DefinitionWithExpressionStatement) conditionalStatement.getIfs().get(2).getBlockStatement().getInstructions().get(0);
//        IdentifierExpression identifierExpression2 = (IdentifierExpression) relationExpression1.getLeft();
//        LiteralInteger literalInteger2 = (LiteralInteger) relationExpression1.getRight();

//        Assert.assertEquals(identifierExpression2.getName(), "x");
//        Assert.assertEquals(literalInteger2.getValue(), 5);
        Assert.assertEquals(relationExpression2, null);

        String name2 = assignment2.getIdentifierName();
        LiteralInteger assignmentInteger2 = (LiteralInteger) assignment2.getExpression();

        Assert.assertEquals(name2, "x");
        Assert.assertEquals(assignmentInteger2.getValue(), 3);
    }

    @Test
    public void testForLoop() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn bool add(int x, int y) {
                              for (int x : a) {
                                  int x = 2;
                              }
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
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getInstructions().size(), 1);

        ForStatement forStatement= (ForStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);
        Assert.assertEquals(forStatement.getType().getType(), Type.INT);
        Assert.assertEquals(forStatement.getIdentifer(), "x");
        Assert.assertEquals(forStatement.getCollectionIdentifer(), "a");
        DefinitionWithExpressionStatement assignment = (DefinitionWithExpressionStatement) forStatement.getBlockStatement().getInstructions().get(0);


        String name = assignment.getIdentifierName();
        LiteralInteger assignmentInteger = (LiteralInteger) assignment.getExpression();

        Assert.assertEquals(name, "x");
        Assert.assertEquals(assignmentInteger.getValue(), 2);
    }

    @Test
    public void testDicitionaryIdentifier() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                fn bool add(int x, int y) {
                                     Dictionary<String, int> dict = | "Krzysztof" : 32,
                                                                    "Klaudia" : 33
                                     |;
                            }
                        """
        );

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();

        DefinitionWithExpressionStatement definition = (DefinitionWithExpressionStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);

        TypeDeclaration typeDeclaration = definition.getType();
        Assert.assertEquals(typeDeclaration.getType(), Type.DICTIONARY);
        Assert.assertEquals(typeDeclaration.getFirstOptionalParam(), Type.STRING);
        Assert.assertEquals(typeDeclaration.getSecondOptionalParam(), Type.INT);

        String name = definition.getIdentifierName();
        Assert.assertEquals(name, "dict");

        LiteralDictionary literalDictionary = (LiteralDictionary) definition.getExpression();
        Map<IExpression, IExpression> map = literalDictionary.getValue();
        Set<IExpression> keySet = map.keySet();
        Iterator<IExpression> iterator = keySet.iterator();
        LiteralString string1 = (LiteralString) iterator.next();
        LiteralString string2 = (LiteralString) iterator.next();

        Assert.assertEquals(string1.getValue(), "Klaudia");
        Assert.assertEquals(string2.getValue(), "Krzysztof");

        LiteralInteger literalInteger1 = (LiteralInteger) map.get(string1);
        LiteralInteger literalInteger2 = (LiteralInteger) map.get(string2);

        Assert.assertEquals(map.get(string1), literalInteger1);
        Assert.assertEquals(map.get(string2), literalInteger2);
    }

    @Test
    public void testSelect() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                fn bool add(int x, int y) {
                                     Dictionary<String, int> dict = | "Krzysztof" : 32,
                                                                    "Klaudia" : 33
                                     |;
                                     
                                     Tuple<String, int> variable = SELECT (dict.key, dict.value) FROM dict WHERE (dict.key == "Krzysztof");
                            }
                        """
        );

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();

        DefinitionWithExpressionStatement definition = (DefinitionWithExpressionStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);

        TypeDeclaration typeDeclaration = definition.getType();
        Assert.assertEquals(typeDeclaration.getType(), Type.DICTIONARY);
        Assert.assertEquals(typeDeclaration.getFirstOptionalParam(), Type.STRING);
        Assert.assertEquals(typeDeclaration.getSecondOptionalParam(), Type.INT);

        String name = definition.getIdentifierName();
        Assert.assertEquals(name, "dict");

        LiteralDictionary literalDictionary = (LiteralDictionary) definition.getExpression();
        Map<IExpression, IExpression> map = literalDictionary.getValue();
        Set<IExpression> keySet = map.keySet();
        Iterator<IExpression> iterator = keySet.iterator();
        LiteralString string1 = (LiteralString) iterator.next();
        LiteralString string2 = (LiteralString) iterator.next();

        Assert.assertEquals(string1.getValue(), "Klaudia");
        Assert.assertEquals(string2.getValue(), "Krzysztof");

        LiteralInteger literalInteger1 = (LiteralInteger) map.get(string1);
        LiteralInteger literalInteger2 = (LiteralInteger) map.get(string2);

        Assert.assertEquals(map.get(string1), literalInteger1);
        Assert.assertEquals(map.get(string2), literalInteger2);

        DefinitionWithExpressionStatement definitionWithExpressionStatement = (DefinitionWithExpressionStatement) functionDefinitionList.get("add").getBody().getInstructions().get(1);

        String tupleName = definitionWithExpressionStatement.getIdentifierName();
        TypeDeclaration declarationType = definitionWithExpressionStatement.getType();

        Assert.assertEquals(tupleName, "variable");
        Assert.assertEquals(declarationType.getType(), Type.TUPLE);
        Assert.assertEquals(declarationType.getFirstOptionalParam(), Type.STRING);
        Assert.assertEquals(declarationType.getSecondOptionalParam(), Type.INT);


        QueryExpression queryExpression = (QueryExpression) definitionWithExpressionStatement.getExpression();

        IdentiferAndFieldReference identiferAndFieldReference = (IdentiferAndFieldReference) queryExpression.getFirstSelectExpression();
        IdentiferAndFieldReference identiferAndFieldReference2 = (IdentiferAndFieldReference) queryExpression.getSecondSelectExpression();

        IdentifierExpression identifer = queryExpression.getFromIdentifer();

        RelationExpression relationExpression = (RelationExpression) queryExpression.getWhereExpression();


        IdentiferAndFieldReference orderBy = (IdentiferAndFieldReference) queryExpression.getOrderByExpression();
        AscOrDESC ascOrDESC = queryExpression.getAscOrDESC();

        Assert.assertEquals(identiferAndFieldReference.getFirstIdentiferName(), "dict");
        Assert.assertEquals(identiferAndFieldReference.getSecondIdentiferName(), "key");
        Assert.assertEquals(identiferAndFieldReference2.getFirstIdentiferName(), "dict");
        Assert.assertEquals(identiferAndFieldReference2.getSecondIdentiferName(), "value");

        Assert.assertEquals(identifer.getName(), "dict");

        IdentiferAndFieldReference identiferAndFieldReferenceLeft = (IdentiferAndFieldReference) relationExpression.getLeft();
        LiteralString StringRight = (LiteralString) relationExpression.getRight();

        Assert.assertEquals(relationExpression.getRelativeOperand(), RelativeType.EQUAL);
        Assert.assertEquals(identiferAndFieldReferenceLeft.getFirstIdentiferName(), "dict");
        Assert.assertEquals(identiferAndFieldReferenceLeft.getSecondIdentiferName(), "key");
        Assert.assertEquals(StringRight.getValue(), "Krzysztof");
    }

    public void testLambda() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                fn bool add(int x, int y) {
                                     Dictionary<String, int> dict = | "Krzysztof" : 32,
                                                                    "Klaudia" : 33
                                     |;
                                     
                                     dict.sort((Tuple<String, int> a,Tuple<String, int> b) => a.get(1) > b.get(1));
                                   }
                        """
        );

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();
        Map<String, FunctionDefinition> functionDefinitionList = program.getFunctionDefinitions();

        DefinitionWithExpressionStatement definition = (DefinitionWithExpressionStatement) functionDefinitionList.get("add").getBody().getInstructions().get(0);

        TypeDeclaration typeDeclaration = definition.getType();
        Assert.assertEquals(typeDeclaration.getType(), Type.DICTIONARY);
        Assert.assertEquals(typeDeclaration.getFirstOptionalParam(), Type.STRING);
        Assert.assertEquals(typeDeclaration.getSecondOptionalParam(), Type.INT);

        String name = definition.getIdentifierName();
        Assert.assertEquals(name, "dict");

        LiteralDictionary literalDictionary = (LiteralDictionary) definition.getExpression();
        Map<IExpression, IExpression> map = literalDictionary.getValue();
        Set<IExpression> keySet = map.keySet();
        Iterator<IExpression> iterator = keySet.iterator();
        LiteralString string1 = (LiteralString) iterator.next();
        LiteralString string2 = (LiteralString) iterator.next();

        Assert.assertEquals(string1.getValue(), "Klaudia");
        Assert.assertEquals(string2.getValue(), "Krzysztof");

        LiteralInteger literalInteger1 = (LiteralInteger) map.get(string1);
        LiteralInteger literalInteger2 = (LiteralInteger) map.get(string2);

        Assert.assertEquals(map.get(string1), literalInteger1);
        Assert.assertEquals(map.get(string2), literalInteger2);

        IdentifierAndLambdaCall lambdaExpression = (IdentifierAndLambdaCall) functionDefinitionList.get("add").getBody().getInstructions().get(1);
        String mainName = lambdaExpression.getName();
        List<Argument> argumentList = lambdaExpression.getArgumentList();
        RelationExpression expression = (RelationExpression) lambdaExpression.getExpression();

        Assert.assertEquals(mainName, "dict");
        Assert.assertEquals(argumentList.size(), 2);

        Assert.assertEquals(argumentList.get(0).getName(), "a");
        Assert.assertEquals(argumentList.get(1).getName(), "b");

        Assert.assertEquals(argumentList.get(0).getType().getType(), Type.TUPLE);
        Assert.assertEquals(argumentList.get(0).getType().getFirstOptionalParam(), Type.STRING);
        Assert.assertEquals(argumentList.get(0).getType().getSecondOptionalParam(), Type.INT);

        Assert.assertEquals(argumentList.get(1).getType().getType(), Type.TUPLE);
        Assert.assertEquals(argumentList.get(1).getType().getFirstOptionalParam(), Type.STRING);
        Assert.assertEquals(argumentList.get(1).getType().getSecondOptionalParam(), Type.INT);

        IdentiferAndMethodCallExpression left = (IdentiferAndMethodCallExpression) expression.getLeft();
        IdentiferAndMethodCallExpression right = (IdentiferAndMethodCallExpression) expression.getRight();
        Assert.assertEquals(expression.getRelativeOperand(), RelativeType.MORE);
        Assert.assertEquals(left.getName(), "a");
        Assert.assertEquals(right.getName(), "b");


        FunctionCall leftMethodCall = left.getMethodCall();
        FunctionCall rightMethodCall = right.getMethodCall();

        Assert.assertEquals(leftMethodCall.getName(), "get");
        Assert.assertEquals(rightMethodCall.getName(), "get");


        LiteralInteger parameter1 = (LiteralInteger) leftMethodCall.getArguments().get(0);
        LiteralInteger parameter2 = (LiteralInteger) rightMethodCall.getArguments().get(0);

        Assert.assertEquals(parameter1.getValue(), 1);
        Assert.assertEquals(parameter2.getValue(), 1);

    }


    @Test
    public void testDuplacatedIdentifer() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn bool add(int x, int y) {
                              for (int x : a) {
                                  int x = 2;
                              }
                          }
                         fn bool add(int x, int y) {
                              for (int x : a) {
                                  int x = 2;
                              }
                          }
                        """
        );

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(DuplicateIdentiferException.class, parser::parseProgram);
    }


    @Test
    public void testSemicolonMissing() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn bool add(int x, int y) {
                              for (int x : a) {
                                  int x = 2
                              }
                          }
                
                        ); 
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }

    @Test
    public void testMissingFunctionReturnType() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn add(int x, int y) {
                              for (int x : a) {
                                  int x = 2;
                              }
                          }
                
                        ); 
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }


    @Test
    public void testMissingParameter() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn add(int x,) {
                              for (int x : a) {
                                  int x = 2;
                              }
                          }
                
                        ); 
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }

    @Test
    public void testMissingparameterIdentifer() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn add(int ) {
                              for (int x : a) {
                                  int x = 2;
                              }
                          }
                
                        ); 
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }

    @Test
    public void testMissingBracket() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn add(int x) {
                              for (int x : a) {
                                  int x = 2;
                              }
                          }
                
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }

    @Test
    public void testWrongBrackets() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn add(int x) {
                              for {int x : a} {
                                  int x = 2;
                              }
                          }
                
                        
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }


    @Test
    public void emptyFor() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn add(int x) {
                              for () {
                                  int x = 2;
                              }
                          }
                
                       
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }


    @Test
    public void testEmptyTuple() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn add(int x) {
                              Tuple<> x = 5;
                          }
              
                      
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }

    @Test
    public void testOperandsBeingNextToEachother() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn add(int x) {
                              int x != +;
                          }
              
                        }
        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }

    @Test
    public void ifWithoutConiditon() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                      fn int add(int x) {
                          if {
                              identifer = "string";
                          } elif (x == 5) {
                            identifer = "variable";
                            }
                          else {
                            return "x";
                          }

                          }
                        """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }


    @Test
    public void elifWithoutConiditon() throws Exception {

        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn int add(int x) {
                            if {
                                identifer = "string";
                            } elif {
                              identifer = "variable";
                              }
                            else {
                              return "x";
                            }

                            }
                          """);

        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Assertions.assertThrows(ParsingException.class, parser::parseProgram);
    }







}
