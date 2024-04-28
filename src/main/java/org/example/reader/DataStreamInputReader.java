package org.example.reader;

import org.example.token.Position;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

public class DataStreamInputReader implements InputReader {

    private static final Logger logger = Logger.getLogger(DataStreamInputReader.class.getName());
    private int lineNumber = 1;
    private int characterNumber = -1;
    private char recentlyLoadedChar = 0;
    private BufferedReader bufferedReader;
    private boolean reloadLastChar = false;

    private final char EOF = '\0';

    private boolean ifEOF = false;
    private boolean fileEnded = false;



    public DataStreamInputReader(String txt) throws FileNotFoundException {
        this.bufferedReader = new BufferedReader(new StringReader(txt));
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
                characterNumber++;
            }
            if (nextChar != (char) -1 && isCharANewLine(nextChar)) {
                lineNumber++;
                characterNumber = -1;
                char nextCharSeq = (char) bufferedReader.read();
                if (nextCharSeq != (char) -1 && !areTwoCharsNewLine(nextChar, nextCharSeq)) {
                    characterNumber++;
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

    public int getLineNumber() {return lineNumber;}
    public int getCharacterNumber() {return  characterNumber;}
}