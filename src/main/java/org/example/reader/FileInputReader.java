package org.example.reader;

import org.example.token.Position;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class FileInputReader implements InputReader {

    private static final Logger logger = Logger.getLogger(FileInputReader.class.getName());
    private final String path;
    private Position position = new Position();
    private char recentlyLoadedChar = 0;
    private BufferedReader bufferedReader;
    private boolean reloadLastChar = false;

    private final char EOF = '\0';

    private boolean ifEOF = false;
    private boolean fileEnded = false;



    public FileInputReader(String path) throws IOException {
        this.path = path;
        boolean isFilePath = path.endsWith(".txt");

        this.bufferedReader = new BufferedReader(new java.io.FileReader(path));
    }


    public Logger getLogger() {
        return logger;
    }

    public char getNextChar() {
        if (ifEOF)
            return EOF;
        try {
            char nextChar;
            if (reloadLastChar) {
                toggleReloadLastChar();
                nextChar = recentlyLoadedChar;
            }
            else {
                nextChar = (char) bufferedReader.read();
                position.nextCharacter();
            }
            if (nextChar != (char) -1 && isCharANewLine(nextChar)) {
                position.nextLine();
                char nextCharSeq = (char) bufferedReader.read();
                if (nextCharSeq != (char) -1 && !areTwoCharsNewLine(nextChar, nextCharSeq)) {
                    position.nextCharacter();
                    recentlyLoadedChar = nextCharSeq;
                    rememberLastCharToBeLoadedNext();
                }
                else {
                    recentlyLoadedChar = '\n';
                }
                return '\n';
            }
            if (nextChar == (char) -1) {
                fileEnded = true;
                closeBuffer();
                return EOF;
            }
            recentlyLoadedChar = nextChar;
            return nextChar;
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        return 0;
    }

    public void rememberLastCharToBeLoadedNext() {
        if (!fileEnded && !isCharANewLine(recentlyLoadedChar)) {
            toggleReloadLastChar();
        }
    }

    private void toggleReloadLastChar() {
        reloadLastChar = !reloadLastChar;
    }

    public boolean isCharANewLine(char x) {
        return x == '\n' || x == '\r';
    }

    public boolean areTwoCharsNewLine(char x, char y) {
        return (x == '\n' && y == '\r') || (x == '\r' && y == '\n');
    }

    private void closeBuffer() {
        try {
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFileEnded() {
        return fileEnded;
    }

    public Position getPosition() {return position;}
}

