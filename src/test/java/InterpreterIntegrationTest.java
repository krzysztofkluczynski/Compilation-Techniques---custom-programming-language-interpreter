import org.example.interpreter.Interpreter;
import org.example.interpreter.InterpretingVisitor;
import org.example.interpreter.error.IncorrectReturnedValueTypeInterpretingException;
import org.example.lexer.LexerImpl;
import org.example.lexer.error.*;
import org.example.parser.ParserImpl;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.reader.DataStreamInputReader;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InterpreterIntegrationTest {

    @Test
    public void basicReturnIntTest() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return 0;
            
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        InterpretingVisitor interpretingVisitor = new InterpretingVisitor(program);
        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(0, result);
    }


    @Test
    public void basicReturnString() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn String main() {
                   return "Hello";
            
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        InterpretingVisitor interpretingVisitor = new InterpretingVisitor(program);
        Interpreter interpreter = new Interpreter(program);

        String result = (String) interpreter.execute();
        Assert.assertEquals("Hello", result);
    }


    @Test
    public void basicReturnFloat() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   return 1.2;
            
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        InterpretingVisitor interpretingVisitor = new InterpretingVisitor(program);
        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(1.2f, result);
    }


    @Test
    public void basicReturnBool() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return true;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(true, result);
    }

    @Test
    public void basicReturnBoolFalse() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return false;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(false, result);
    }

    @Test
    public void returnList() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn List<int> main() {
                   return [1, 2];
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        List<Integer> result = (List<Integer>) interpreter.execute();
        Assert.assertEquals(result.get(0), 1);
        Assert.assertEquals(result.get(1), 2);
    }


    @Test
    public void returnDictionary() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn Dictionary<int, int> main() {
                   return | 1: 1, 2: 2 |; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Map<Integer, Integer> result = (Map<Integer, Integer>) interpreter.execute();
        Assert.assertEquals(result.get(1), 1);
        Assert.assertEquals(result.get(2), 2);
    }

    @Test
    public void returnTuple() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn Tuple<String, String> main() {
                   return ("Hello", "World"); 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        List<String> result = (List<String>) interpreter.execute();
        Assert.assertEquals(result.get(0), "Hello");
        Assert.assertEquals(result.get(1), "World");
    }


    @Test
    public void checkWrongReturnTypeExcpetion() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return 1;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(IncorrectReturnedValueTypeInterpretingException.class, interpreter::execute);
    }


    @Test
    public void checkWrongReturnTypeExcpetionSecond() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn String main() {
                   return false;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(IncorrectReturnedValueTypeInterpretingException.class, interpreter::execute);
    }


}
