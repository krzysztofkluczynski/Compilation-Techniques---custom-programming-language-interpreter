package org.example.lexer;

import org.example.lexer.error.*;
import org.example.reader.InputReader;
import org.example.token.*;
import org.xml.sax.ErrorHandler;

import java.io.IOException;


public class LexerImpl implements Lexer {

    private final int INTEGERMAXVALUE;
    private final int DECIMALPARTMAXVALUE;

    private final int STRINGMAXLENGTH;
    private final int MAXIDENTIFIERLENGTH;


    private final InputReader reader;
    private Position currentPosition;

    private Token token;
    private char currentChar;

    public LexerImpl(InputReader reader) {
        this.reader = reader;
        this.INTEGERMAXVALUE = 10000000;
        this.DECIMALPARTMAXVALUE = 99999999;
        this.STRINGMAXLENGTH = 300;
        this.MAXIDENTIFIERLENGTH = 100;
    }

    @Override
    public Token next() throws NumberMaxValueExceededException, ReachedEOFException, StringMaxSizeExceeded, IOException, UnkownTokenException, IdentifierTooLongException {
        currentChar = reader.getNextChar();
        skipWhitespace();
        currentPosition = getPosition();

        if (tryBuildEndOfFile() || tryBuildNumber() || tryBuildString() || tryBuildCommentOrOperandToken() || tryBuildIdentifierOrKeywordOrBoolean())
        {
            return token;
        } else {
            throw new UnkownTokenException("Unknown Token");
        }

    }


    private void skipWhitespace() {
        while (Character.isWhitespace(currentChar)) {
            currentChar = reader.getNextChar();
        }
    }

    private boolean tryBuildEndOfFile() {
        if (!reader.isFileEnded()) {
            return false;
        }
        token = new KeywordToken(TokenType.END_OF_FILE, currentPosition);
        return true;
    }

    private boolean tryBuildNumber() throws NumberMaxValueExceededException {
        if (!Character.isDigit(currentChar)) {
            return false;
        }

        StringBuilder numberBuilder = new StringBuilder();
        numberBuilder.append(currentChar);
        boolean hasDot = false;

        while (Character.isDigit(currentChar = reader.getNextChar()) || currentChar == '.') {
            if (currentChar == '.') {
                if (hasDot) { // only one dot is allowed
                    throw new NumberFormatException("Invalid number format."); //TODO - customowy wyjatek?
                }
                hasDot = true;
            }
            numberBuilder.append(currentChar);
        }


        if (hasDot) {
            float value = Float.parseFloat(numberBuilder.toString());
            String[] parts = numberBuilder.toString().split("\\.");
            if (parts.length > 1 && Integer.parseInt(parts[1]) > DECIMALPARTMAXVALUE) {
                throw new NumberMaxValueExceededException("Float value do not match alloweed range: " + DECIMALPARTMAXVALUE,  String.valueOf(value));
            }
            if (value > INTEGERMAXVALUE) {
                throw new NumberMaxValueExceededException("Float value do not match allowed range: " + INTEGERMAXVALUE, String.valueOf(value));
            }
            token = new FloatToken(currentPosition, value);
            reader.rememberLastCharToBeLoadedNext();
            return true;
        } else {
            int value = Integer.parseInt(numberBuilder.toString());
            if (value > INTEGERMAXVALUE) {
                throw new NumberMaxValueExceededException("Integer value exceeds max value: " + INTEGERMAXVALUE, String.valueOf(value));
            }
            token = new IntegerToken(currentPosition, value);
            reader.rememberLastCharToBeLoadedNext();
            return true;
        }


    }

    private boolean tryBuildString() throws ReachedEOFException, StringMaxSizeExceeded {
        if(String.valueOf(currentChar).equals("\"")) {
            StringBuilder stringBuilder = new StringBuilder();
            currentChar = reader.getNextChar();
            while(!"\"".equals(String.valueOf(currentChar)) && !reader.isFileEnded()) {
                if (reader.isCharANewLine(currentChar)) {
                    char previousChar = currentChar;
                    currentChar = reader.getNextChar();
                    if (reader.areTwoCharsNewLine(previousChar, currentChar)) {
                        currentChar = reader.getNextChar();
                    }
                    stringBuilder.append("\\n");
                }
                else {
                    stringBuilder.append(currentChar);
                    currentChar = reader.getNextChar();
                }
            }
            if (reader.isFileEnded()) {
                throw new ReachedEOFException("Reached end of file while parsing String");
            } else if (stringBuilder.length() > STRINGMAXLENGTH) {
                throw new StringMaxSizeExceeded("String max size excceed while parsing string max is: " + STRINGMAXLENGTH);
            } else {
                token =  new StringToken(currentPosition, stringBuilder.toString());
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean tryBuildCommentOrOperandToken() throws IOException, ReachedEOFException {
        if (currentChar == '/') {
            char nextChar = reader.getNextChar();
            if (nextChar == '/') {
                consumeLineComment();
                return true;
            } else if (nextChar == '*') {
                consumeBlockComment();
                return true;
            }
            reader.rememberLastCharToBeLoadedNext();
        }

        // Sprawdź złożone operandy
        String lexeme = Character.toString(currentChar);
        char nextChar = reader.getNextChar();
        String potentialDoubleLexeme = lexeme + nextChar;

        // Sprawdź, czy dłuższy leksem odpowiada typowi tokena
        TokenType type = LexerUtils.getTokenType(potentialDoubleLexeme);
        if (type != null) {
            token = new KeywordToken(type, currentPosition);
            return true;
        } else {
            reader.rememberLastCharToBeLoadedNext();
        }

        type = LexerUtils.getTokenType(lexeme);
        if (type != null) {
            token = new KeywordToken(type, currentPosition);
            return true;
        }

        return false;
    }

    private void consumeLineComment() {
        while ((currentChar = reader.getNextChar()) != '\n' && currentChar != '\0') {}
        token = new KeywordToken(TokenType.ONE_LINE_COMMENT, currentPosition);
    }

    private void consumeBlockComment() throws IOException, ReachedEOFException {
        while (true) {
            currentChar = reader.getNextChar();
            if (currentChar == '\0') {  // Sprawdzenie końca pliku
                throw new ReachedEOFException("Reached EOF while parsing a comment");
            }
            if (currentChar == '*') {
                char nextChar = reader.getNextChar();
                if (nextChar == '/') {
                    break;
                }
                reader.rememberLastCharToBeLoadedNext();
            }
        }
        token = new KeywordToken(TokenType.MULTI_LINE_COMMENT, currentPosition);
    }
    private boolean tryBuildIdentifierOrKeywordOrBoolean() throws IOException, IdentifierTooLongException {
        StringBuilder builder = new StringBuilder();
        if (Character.isLetter(currentChar) || currentChar == '_') {  // Identifiers can start with a letter or underscore
            builder.append(currentChar);
            while ((currentChar = reader.getNextChar()) != '\0' &&
                    (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                builder.append(currentChar);
            }
            //reader.rememberLastCharToBeLoadedNext();  // Put back the last character that is not part of the identifier

            String lexeme = builder.toString();
            if (lexeme.length() > MAXIDENTIFIERLENGTH) {
                throw new IdentifierTooLongException("Identifer too long");
            }
            TokenType type = LexerUtils.getTokenType(lexeme);

            if (type != null) {
                token = new KeywordToken(type, currentPosition);
                reader.rememberLastCharToBeLoadedNext();
                return true;
            } else if (lexeme.equals("true") || lexeme.equals("false")) {
                token = new BoolToken(currentPosition, Boolean.valueOf(lexeme));
                reader.rememberLastCharToBeLoadedNext();
                return true;
            } else {
                // If it's not a keyword or boolean literal, it's an identifier
                token = new KeywordToken(TokenType.IDENTIFIER, currentPosition);
                reader.rememberLastCharToBeLoadedNext();
                return true;
            }
        }
        return false;
    }

    private Position getPosition() {
        return reader.getPosition();
    }

}
