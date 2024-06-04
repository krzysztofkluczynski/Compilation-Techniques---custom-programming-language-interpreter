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
    public void NoSuchVariable() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn int main() {
                   int counter = 6;
                   
                  
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

    @Test
    public void invalidLogicStatement() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn String main() {
                   return 1 > "a";
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidLogicStatementInterpretingException.class, interpreter::execute);
    }

    @Test
    public void invalidLogicStatementTwo() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                   return 3 <= 3.0;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidLogicStatementInterpretingException.class, interpreter::execute);
    }



    @Test
    public void incorrectPrint() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                    print(1, 2, 3);
                   return true;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(IncorrectDefaultPrintFunctionCallInterpretingException.class, interpreter::execute);
    }

    @Test
    public void incorrectTypePrint() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                        
                fn bool main() {
                    print(1);
                   return true;
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
    public void invalidFunctionCall() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                fn int add(int a, int b) {
                    return a + b;
                }
                        
                fn bool main() {
                   int y = add(1);
                   return true;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidFunctionCallInterpretingException.class, interpreter::execute);
    }

    @Test
    public void invalidFunctionCallTwo() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                fn int add(int a, int b) {
                    return a + b;
                }
                        
                fn bool main() {
                   int y = add(1, 2, 3);
                   return true;
                }
            """
        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidFunctionCallInterpretingException.class, interpreter::execute);
    }

    @Test
    public void invalidFunctionCallWrongValueTypeThree() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                fn int add(int a, int b) {
                    return a + b;
                }
                        
                fn bool main() {
                   int y = add(1, 2.5);
                   return true;
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
    public void TestGetMethodWrongArguments() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int main() {
                               List<int> f = [1, 2, 3];
                               return f.get(2, 0);
                             }
                        """

        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidNumberofArgumentsException.class, interpreter::execute);
    }

    @Test
    public void TestGetMethodWrongArgumentsTwo() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int main() {
                               List<int> f = [1, 2, 3];
                               return f.set();
                             }
                        """

        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidNumberofArgumentsException.class, interpreter::execute);
    }

    @Test
    public void TestInvalidTypeForMethodCall() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int main() {
                               List<int> f = [1, 2, 3];
                               return f.ifexists(1);
                             }
                        """

        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidTypeForMethodCallInterpretingException.class, interpreter::execute);
    }

    @Test
    public void TestInvalidTypeForMethodCallTwo() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int main() {
                               int f = 1;
                               return f.get(1);
                             }
                        """

        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(InvalidTypeForMethodCallInterpretingException.class, interpreter::execute);
    }

    @Test
    public void LocalVariableRepaeted() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int main() {
                               int f = 1;
                               String f;
                               return f.get(1);
                             }
                        """

        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(LocalVariableRepeatedInterpretingException.class, interpreter::execute);
    }


    @Test
    public void NoSuchFunction() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int main() {
                               int f = 1;
                               String f = call(f);
                               return f.get(1);
                             }
                        """

        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(NoSuchFunctionInterpretingException.class, interpreter::execute);
    }

    @Test
    public void NoMain() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int add() {
                               int f = 1;
                               String f = call(f);
                               return f.get(1);
                             }
                        """

        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(NoMainFunctionInterpretingException.class, interpreter::execute);
    }

    @Test
    public void NoValueReturnedFromFunction() throws Exception {
        DataStreamInputReader reader = new DataStreamInputReader(
                """
                            fn int add() {
                               int f = 1;
                             }
                        """

        );
        LexerImpl lexer = new LexerImpl(reader);
        ParserImpl parser = new ParserImpl(lexer);
        Program program = parser.parseProgram();

        Interpreter interpreter = new Interpreter(program);

        Assert.assertThrows(NoMainFunctionInterpretingException.class, interpreter::execute);
    }




}
