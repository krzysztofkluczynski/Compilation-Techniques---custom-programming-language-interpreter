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
