import org.example.lexer.Lexer;
import org.example.lexer.LexerImpl;
import org.example.lexer.error.*;
import org.example.parser.Enum.Type;
import org.example.parser.ParserImpl;
import org.example.parser.Structure.OtherComponents.FunctionDefinition;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.parser.Structure.Statement.AssignmentWithQueryStatement;
import org.example.reader.DataStreamInputReader;
import org.example.token.IntegerToken;
import org.example.token.Token;
import org.example.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

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
//        Assert.assertEquals(functionDefinitionList.get("function").getBody().getInstructions().get(0));
//        Assert.assertEquals(functionDefinitionList.get("function").getBody().getInstructions().get(1));

    }

    @Test
    public void testSimpleboolFunction() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        fn bool add(int x, int y) {
                            int z = 3*4;
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
        Assert.assertEquals(functionDefinitionList.get("add").getBody().getInstructions().size(), 2);
//        Assert.assertEquals(functionDefinitionList.get("function").getBody().getInstructions().get(0));
//        Assert.assertEquals(functionDefinitionList.get("function").getBody().getInstructions().get(1));

    }

}
