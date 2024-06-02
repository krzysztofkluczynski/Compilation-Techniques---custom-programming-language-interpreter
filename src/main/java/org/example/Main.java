package org.example;

import org.example.interpreter.Interpreter;
import org.example.interpreter.InterpretingVisitor;
import org.example.lexer.Lexer;
import org.example.lexer.LexerImpl;
import org.example.parser.ParserImpl;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.reader.DataStreamInputReader;
import org.example.token.TokenType;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {


        if (args.length < 1) {
                throw new Exception("At least file path must be provided as an argument.");
            }

        Lexer lexer;
        try {
            String filePath = args[0];
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            DataStreamInputReader reader = new DataStreamInputReader(content);
        if (args.length == 5) {
            lexer = new LexerImpl(reader, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        } else {
            lexer = new LexerImpl(reader);
        }

            ParserImpl parser = new ParserImpl(lexer);

//            for (int count = 0; count < 70; count++) {
//                TokenType t = lexer.next().getType();
//                System.out.println(count + ". " + t);
//                if (t == TokenType.END_OF_FILE) {
//                    break;
//                }
//            }
            Program program = parser.parseProgram();
            InterpretingVisitor interpretingVisitor = new InterpretingVisitor(program);
            Interpreter interpreter = new Interpreter(program);

            interpreter.execute();

        } catch (FileNotFoundException fnfe) {
            Logger.getLogger("File not found exception").warning(fnfe.getMessage());
            System.out.println("Error: File not found - " + fnfe.getMessage());
        } catch (Exception ex) {
            Logger.getLogger("General exception").warning(ex.getMessage());
            System.out.println("Error: " + ex.getMessage());
        }
    }
}