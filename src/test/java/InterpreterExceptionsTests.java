import org.example.interpreter.Interpreter;
import org.example.interpreter.error.*;
import org.example.lexer.LexerImpl;
import org.example.parser.ParserImpl;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.reader.DataStreamInputReader;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class InterpreterExceptionsTests {

    @Test
    public void TestWhileVariableBeyondTheScope() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   int counter = 6;
                   while (counter > 2) {
                       int counter2 = 1;
                       counter = counter - 1;
                   }
                  
                   return counter2;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(NoSuchVariableInterpretingException.class, interpreter::execute);
    }

    @Test
    public void TestIntegerTypeDoNotMatchException() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn String main() {
                   int f = "a";
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(VariableValueTypeInterpretingException.class, interpreter::execute);

    }

    @Test
    public void TestEmptyListTypeDoNotMatchException() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn List<String> main() {
                   List<String> f = [1];
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(VariableValueTypeInterpretingException.class, interpreter::execute);
    }

    @Test
    public void TestTupleTypeDoNotMatchException() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn Tuple<String, String> main() {
                   Tuple<String, String> f;
                   f = ("a", 1);
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(VariableValueTypeInterpretingException.class, interpreter::execute);
    }

    @Test
    public void negatedStringException() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return not "Hello";
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InterpretingException.class, interpreter::execute);

    }

    @Test
    public void DivisionByZeroExceptionFloat() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return 3.0 / 0.0; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(DivisionByZeroInterpretingException.class, interpreter::execute);
    }

    @Test
    public void DivisionByZeroExceptiom() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return 3 / 0; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(DivisionByZeroInterpretingException.class, interpreter::execute);
    }

    @Test
    public void WrongTypesInExpressionExceptionOne() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn String main() {
                   return "Hello" + 3; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidExpressionInterpretingException.class, interpreter::execute);
    }

    @Test
    public void WrongTypesInExpressionExceptionTwo() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   return 3.5 + 3; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidExpressionInterpretingException.class, interpreter::execute);
    }

    @Test
    public void WrongTypesInExpressionExceptionThree() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return 3 / true; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidExpressionInterpretingException.class, interpreter::execute);
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
