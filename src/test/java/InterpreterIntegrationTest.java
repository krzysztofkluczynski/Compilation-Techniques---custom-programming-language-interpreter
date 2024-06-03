import org.example.interpreter.Interpreter;
import org.example.interpreter.InterpretingVisitor;
import org.example.interpreter.error.*;
import org.example.interpreter.model.Pair;
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

        Pair pair = (Pair) interpreter.execute();
        Assert.assertEquals(pair.getFirst(), "Hello");
        Assert.assertEquals(pair.getSecond(), "World");
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


    @Test
    public void returnOrExpression() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return false and true; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, false);
    }

    @Test
    public void returnAndExpression() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return false or true; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, true);
    }

    @Test
    public void returnGreaterIntegers() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return 2 > 1; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, true);
    }


    @Test
    public void returnSmallerIntegers() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return 2 < 1; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, false);
    }

    @Test
    public void returnsmallerOrEqualIntegers() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return 2 <= 1; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, false);
    }

    @Test
    public void returnGreaterFloats() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return 2.5 > 1.3; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, true);
    }


    @Test
    public void returnGreaterStrings() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return "a" > "b";  
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, false); // a is lexically smaller than b
    }

    @Test
    public void returnEqualsInteger() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return 1 == 2; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, false);
    }


    @Test
    public void returnAddIntegers() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return 2+3; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 5);
    }


    @Test
    public void returnSubtractIntegers() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return 3-1; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 2);
    }

    @Test
    public void returnMulipltyIntegers() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return 2*3; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 6);
    }

    @Test
    public void returnDivideIntegers() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return 3/2; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 1);
    }

    @Test
    public void returnAddFloat() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   return 3.5 + 2.7; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(result, 6.2f, 0.0001);
    }


    @Test
    public void returnSubtractFloat() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   return 3.5 - 2.7; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(0.8f, result, 0.0001);
    }

    @Test
    public void returnMultiplyFloat() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   return 3.0 * 2.5; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(result, 7.5f, 0.0001);
    }

    @Test
    public void returnDivideFloat() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   return 3.0 / 1.5; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(result, 2.0f, 0.0001);
    }

    @Test
    public void returnAddString() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn String main() {
                   return "Hello" + " World"; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        String result = (String) interpreter.execute();
        Assert.assertEquals(result, "Hello World");
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
    public void NegatedInteger() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return -1; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, -1);
    }

    @Test
    public void NegatedIntegerAdd() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return -5 + 4; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, -1);
    }


    @Test
    public void NegatedFloat() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return -5 + 4;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, -1);
    }

    @Test
    public void negatedBoolean() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return not true; 
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, false);
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
    public void stringToIntCast() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return $int "2";
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 2);

    }

    @Test
    public void stringToFloatCast() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   return $float "2.0";
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(result, 2.0f);

    }

    @Test
    public void IntToFloatCast() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   return $float 2;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(result, 2.0f);

    }

    @Test
    public void FloatToIntCast() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   return $int 2.3;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 2);

    }

    @Test
    public void IntToStringCast() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn String main() {
                   return $String 2;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        String result = (String) interpreter.execute();
        Assert.assertEquals(result, "2");

    }


    @Test
    public void TestIntegerDefinition() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   int a = 2;
                   return a;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 2);

    }


    @Test
    public void TestFloatDefinition() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   float f = 2.5;
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(result, 2.5f);

    }


    @Test
    public void TestBoolDefinition() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   bool f = true;
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, true);

    }

    @Test
    public void TestStringDefinition() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   String f = "2";
                   return $int f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 2);

    }

    @Test
    public void TestStringDeclaration() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   String f;
                     f = "2";
                   return $int f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 2);

    }


    @Test
    public void TestFloatDeclaration() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn float main() {
                   int f;
                   f = 2;
                   return $float f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        float result = (float) interpreter.execute();
        Assert.assertEquals(result, 2.0f);

    }

    @Test
    public void TestDictionaryDefinition() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn Dictionary<String,int> main() {
                   Dictionary<String,int> f = | "a": 1, "b": 2|;
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Map result = (Map) interpreter.execute();
        Assert.assertEquals(result, Map.of("a", 1, "b", 2));

    }

    @Test
    public void TestDictionaryDeclaration() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn Dictionary<String,int> main() {
                   Dictionary<String,int> f;
                   f = | "a": 1, "b": 3|;
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Map result = (Map) interpreter.execute();
        Assert.assertEquals(result, Map.of("a", 1, "b", 3));

    }


    @Test
    public void TestListDefinition() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn List<String> main() {
                   List<String> f = ["a", "b"];
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        List result = (List) interpreter.execute();
        Assert.assertEquals(result, List.of("a", "b"));

    }

    @Test
    public void TestListDeclaration() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn List<String> main() {
                   List<String> f;
                   f = [ "a", "b"];
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        List result = (List) interpreter.execute();
        Assert.assertEquals(result, List.of("a", "b"));
    }

    @Test
    public void TestTupleDefinition() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn Tuple<String, String> main() {
                   Tuple<String, String> f = ("a", "b");
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Pair result = (Pair) interpreter.execute();
        Assert.assertEquals(result.getSecond(), "b");
        Assert.assertEquals(result.getFirst(), "a");
    }

    @Test
    public void TestTupleDeclaration() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn Tuple<String, String> main() {
                   Tuple<String, int> f;
                   f = ("a", 1);
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Pair result = (Pair) interpreter.execute();
        Assert.assertEquals(result.getSecond(), 1);
        Assert.assertEquals(result.getFirst(), "a");
    }

    @Test
    public void TestEmptyList() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn List<String> main() {
                   List<String> f;
                   f = [];
                   return f;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        List result = (List) interpreter.execute();
        Assert.assertEquals(result, List.of());
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
    public void TestWhileStatement() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   int counter = 0;
                   while (counter < 5) {
                       counter = counter + 1;
                   }
                 
                   return counter;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 5);
    }

    @Test
    public void TestWhileStatementTwo() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   int counter = 0;
                   while (counter <= 5) {
                       counter = counter + 1;
                   }
                 
                   return counter;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 6);
    }

    @Test
    public void TestWhileStatementThree() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   int counter = 6;
                   while (counter > 2) {
                       counter = counter - 1;
                   }
                  
                   return counter;
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 2);
    }

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
    public void testIfStatement() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                     int x;
                     if (true) {
                          x = 3;
                     } else {
                          x = 4;
                     }
                   return x;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 3);
    }


    @Test
    public void testIfStatementFalse() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                     int x;
                     if (false) {
                          x = 3;
                     } else {
                          x = 4;
                     }
                   return x;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 4);
    }


    @Test
    public void testBothFalse() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                     int x;
                     if (false) {
                          x = 3;
                     } elif (false) {
                          x = 4;
                     } else {
                          x = 5;
                     }
                   return x;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 5);
    }

@Test
    public void testSingleIf() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                     int x;
                     if (5 > 3) {
                          x = 3;
                     } 
                   return x;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 3);
    }

    @Test
    public void testSingleIfFalse() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                     int x = 2;
                     if (5 < 3) {
                          x = 3;
                     } 
                   return x;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 2);
    }

    @Test
    public void testForLoop() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                     List<int> list = [1, 2, 3, 4, 5];
                     int x = 1;
                     for (int i : list) {
                          if (i == 3) {
                              x = i;
                          }
                     }
                   return x;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 3);
    }


    @Test
    public void testForLoopTwo() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                     List<int> list = [1, 2, 3, 4, 5];
                     int x = 1;
                     for (int i : list) {
                          x = i;
                     }
                   return x;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 5);
    }


    @Test
    public void testForLoopReference() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn List<int> main() {
                     List<int> list = [1, 2, 3, 4, 5];
                     int x = 1;
                     for (int i : list) {
                          print($String i);
                     }
                   return list;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        List result = (List) interpreter.execute();
        Assert.assertEquals(result, List.of(1, 2, 3, 4, 5));
    }


    @Test
    public void TestDictionaryForLoop() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                    
                            fn Tuple<String, int> main() {
                                 Dictionary<String, int> var_dict = |
                                                                                  "dog": 3,
                                                                                  "cat": 4,
                                                                                  "cow": 1,
                                                                                  "hamster": 6
                                                                              |;
                                                                              
                                 for (Tuple<String, int> t : var_dict) {
                                      return t;
                                 }
                             
                            }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Pair result = (Pair) interpreter.execute();
        Assert.assertEquals(result.getFirst(), "dog");
        Assert.assertEquals(result.getSecond(), 3);

    }


    @Test
    public void TestDictionaryForLoopTypesExcpetion() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                    
                            fn Tuple<String, int> main() {
                                 Dictionary<String, int> var_dict = |
                                                                                  "dog": 3,
                                                                                  "cat": 4,
                                                                                  "cow": 1,
                                                                                  "hamster": 6
                                                                              |;
                                                                              
                                 for (Tuple<String, String> t : var_dict) {
                                      return t;
                                 }
                             
                            }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(UnableToAssignVariableException.class, interpreter::execute);
    }


    @Test
    public void TestListForLoopTypesExcpetion() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                    
                            fn Tuple<String, int> main() {
                                 List<int> var_dict = [1, 2, 3];
                                                                              
                                 for (List<String> s : var_dict) {
                                      return s;
                                 }
                             
                            }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(UnableToAssignVariableException.class, interpreter::execute);
    }

    @Test
    public void TestListMethodGet() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   List<int> f = [1, 2, 3];
                   return f.get(2);
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 3);
    }

    @Test
    public void TestListMethodAdd() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   List<int> f = [1, 2, 3];
                   f.add(4);
                   return f.get(3);
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 4);
    }

    @Test
    public void TestListMethodSet() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   List<int> f = [1, 2, 3];
                   f.set(2, 4);
                   return f.get(2);
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 4);
    }


    @Test
    public void TestListMethodDelete() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   List<int> f = [1, 2, 3];
                   f.delete(2);
                   return f.get(1);
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 3);
    }


    @Test
    public void TestTupleMethodGet() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn String main() {
                   Tuple<String, int> f = ("a", 1);
                   return f.get(0);
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        String result = (String) interpreter.execute();
        Assert.assertEquals(result, "a");
    }

    @Test
    public void TestTupleMethodGetException() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                    //comment
                   Tuple<String, int> f = ("a", 1);
                   f.set(1, 5);
                   return f.get(1);
                 }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 5);
    }


    @Test
    public void TestDictionaryMethodGet() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                    
                            fn int main() {
                               Dictionary<String, int> var_dict = |
                                               "dog": 3,
                                               "cat": 4,
                                               "cow": 5,
                                               "hamster": 6
                                           |;
                                           
                               
                               return var_dict.get("cow");
                             }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 5);
    }


    @Test
    public void TestDictionaryMethodSet() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                    
                            fn int main() {
                               Dictionary<String, int> var_dict = |
                                               "dog": 3,
                                               "cat": 4,
                                               "cow": 5,
                                               "hamster": 6
                                           |;
                                           
                               var_dict.set("cow", 10);
                               return var_dict.get("cow");
                             }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 10);
    }

    @Test
    public void TestDictionaryMethodDelete() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                    
                            fn bool main() {
                               Dictionary<String, int> var_dict = |
                                               "dog": 3,
                                               "cat": 4,
                                               "cow": 5,
                                               "hamster": 6
                                           |;
                                           
                               var_dict.delete("cow");
                               return var_dict.ifexists("cow");
                             }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        boolean result = (boolean) interpreter.execute();
        Assert.assertEquals(result, false);
    }


    @Test
    public void TestDictionaryMethodAdd() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                                    
                            fn int main() {
                               Dictionary<String, int> var_dict = |
                                               "dog": 3,
                                               "cat": 4,
                                               "cow": 5,
                                               "hamster": 6
                                           |;
                                           
                               var_dict.add("bird", 10);
                               return var_dict.get("bird");
                             }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, 10);
    }


    @Test
    public void TestFunctionCall() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int sum(List<int> list) {
                                int sum = 0;
                                for (int i : list) {
                                    sum = sum + i;
                                }
                                return sum;
                             }
                                    
                             fn int main() {
                               List<int> f = [1, 2, 3];
                               int x = sum(f);
                                         
                               return -x + 1 * 3;
                             }
                        """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        int result = (int) interpreter.execute();
        Assert.assertEquals(result, -3);
    }

//    @Test
//    public void TestFunctionCallTwo() throws Exception {
//        DataStreamInputReader reader = new DataStreamInputReader(
//                """
//                            fn void increment(int x) {
//                                x = x + 1;
//                             }
//
//                             fn int main() {
//                               int x = 3;
//                               increment(x);
//                               return x;
//                             }
//                        """
//        );
//        LexerImpl lexer = new LexerImpl(reader);
//        ParserImpl parser = new ParserImpl(lexer);
//        Program program = parser.parseProgram();
//
//        Interpreter interpreter = new Interpreter(program);
//
//        int result = (int) interpreter.execute();
//        Assert.assertEquals(result, 4);
//    }


}
