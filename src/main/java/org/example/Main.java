package org.example;

import org.example.lexer.Lexer;
import org.example.lexer.LexerImpl;
import org.example.reader.FileInputReader;
import org.example.token.TokenType;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new Exception("A file path must be provided as an argument.");
            }
            String filePath = args[0];
            FileInputReader fileReader = new FileInputReader(filePath);
            Lexer lexer = new LexerImpl(fileReader);

            for (int count = 0; count < 25; count++) {
                TokenType t = lexer.next().getType();
                System.out.println(t);
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