package org.example.reader;

import org.example.token.Position;


public interface InputReader {

        public Position getPosition();

        public char getNextChar();

        public boolean isFileEnded();

        public boolean isCharANewLine(char x);
        public boolean areTwoCharsNewLine(char x, char y);

        public void rememberLastCharToBeLoadedNext();


}
