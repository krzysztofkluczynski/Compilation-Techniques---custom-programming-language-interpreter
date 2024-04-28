package org.example;

import org.example.lexer.Lexer;
import org.example.lexer.LexerImpl;
import org.example.reader.DataStreamInputReader;
import org.example.token.TokenType;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
                throw new Exception("A file path must be provided as an argument.");
            }

        try {
            String filePath = args[0];
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            DataStreamInputReader reader = new DataStreamInputReader(content);
            Lexer lexer = new LexerImpl(reader);

            for (int count = 0; count < 70; count++) {
                TokenType t = lexer.next().getType();
                System.out.println(count + ". " + t);
                if (t == TokenType.END_OF_FILE) {
                    break;
                }
            }
        } catch (FileNotFoundException fnfe) {
            Logger.getLogger("File not found exception").warning(fnfe.getMessage());
            System.out.println("Error: File not found - " + fnfe.getMessage());
        } catch (Exception ex) {
            Logger.getLogger("General exception").warning(ex.getMessage());
            System.out.println("Error: " + ex.getMessage());
        }
    }
}