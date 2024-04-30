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

    public LexerImpl(InputReader reader, int intMaxValue, int decimalMaxValue, int stringMaxLength, int identifierMaxLength) {
        this.reader = reader;
        this.INTEGERMAXVALUE = intMaxValue;
        this.DECIMALPARTMAXVALUE = decimalMaxValue;
        this.STRINGMAXLENGTH = stringMaxLength;
        this.MAXIDENTIFIERLENGTH = identifierMaxLength;
    }

    @Override
    public Token next() throws NumberMaxValueExceededException, ReachedEOFException, StringMaxSizeExceeded, IOException, UnkownTokenException, IdentifierTooLongException {
        currentChar = reader.getNextChar();
        skipWhitespace();
        currentPosition = new Position(reader.getLineNumber(), reader.getCharacterNumber());

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
        if (Character.isDigit(currentChar)) {
            long value = 0;
            while(Character.isDigit(currentChar)) {
                if (value * 10 + (currentChar - '0') > this.INTEGERMAXVALUE) {
                    throw new NumberMaxValueExceededException("Value of int is too big! Max value is: "+  this.INTEGERMAXVALUE, String.valueOf(value));
                }
                value = value * 10;
                value += currentChar - '0';
                currentChar = reader.getNextChar();
            }

            boolean hasDecimalValue = false;
            long decimalValue = 0;
            int decimalCounter = 0;
            if (currentChar == '.') {
                hasDecimalValue = true;
                currentChar = reader.getNextChar();
                while(Character.isDigit(currentChar)) {
                    if (decimalValue * 10 + (currentChar - '0') > DECIMALPARTMAXVALUE) {
                        throw new NumberMaxValueExceededException("Value of decimal is too big! Max value is: "+  DECIMALPARTMAXVALUE, String.valueOf(value));
                    }
                    decimalValue = decimalValue * 10;
                    decimalValue += currentChar - '0';
                    decimalCounter += 1;
                    currentChar = reader.getNextChar();
                }
            }
            if (hasDecimalValue && decimalCounter == 0) {
                StringBuilder stringBuilderInvalidValue = new StringBuilder(String.valueOf(value) + '.');
                while (!reader.isFileEnded() && !Character.isWhitespace(currentChar)) {
                    stringBuilderInvalidValue.append(currentChar);
                    currentChar = reader.getNextChar();
                }
                reader.rememberLastCharToBeLoadedNext();
                return false;

            } else if (hasDecimalValue) {
                reader.rememberLastCharToBeLoadedNext();
                double floatValue = value + (decimalValue / Math.pow(10, decimalCounter));
                token =  new FloatToken(currentPosition, (float) floatValue);
                return true;
            } else {
                reader.rememberLastCharToBeLoadedNext();
                token = new IntegerToken(currentPosition, (int) value);
                return true;
            }
        } else return false;

    }

    private boolean tryBuildString() throws ReachedEOFException, StringMaxSizeExceeded {
        if (String.valueOf(currentChar).equals("\"")) {
            StringBuilder stringBuilder = new StringBuilder();
            boolean insideSingleQuotes = false;  // Flag to check if inside single quotes
            currentChar = reader.getNextChar();
            while (!"\"".equals(String.valueOf(currentChar)) && !reader.isFileEnded()) {
                if (currentChar == '\'') {  // Toggle the insideSingleQuotes
                    insideSingleQuotes = !insideSingleQuotes;
                }
                if (!insideSingleQuotes && reader.isCharANewLine(currentChar)) {
                    char previousChar = currentChar;
                    currentChar = reader.getNextChar();
                    if (reader.areTwoCharsNewLine(previousChar, currentChar)) {
                        currentChar = reader.getNextChar();
                    }
                    stringBuilder.append("\\n");
                } else {
                    if (stringBuilder.length() > STRINGMAXLENGTH) {
                        throw new StringMaxSizeExceeded("String max size exceeded while parsing string max is: " + STRINGMAXLENGTH);
                    }
                        stringBuilder.append(currentChar);
                        currentChar = reader.getNextChar();
                    }
                }
                if (reader.isFileEnded()) {
                    throw new ReachedEOFException("Reached end of file while parsing String");
                } else {
                    token = new StringToken(currentPosition, stringBuilder.toString());
                    return true;
                }

            }
        return false;
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

        String lexeme = Character.toString(currentChar);
        char nextChar = reader.getNextChar();
        String potentialDoubleLexeme = lexeme + nextChar;

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
                if (builder.toString().length() >= MAXIDENTIFIERLENGTH) {
                    throw new IdentifierTooLongException("Identifer too long");
                }
                builder.append(currentChar);
            }

            String lexeme = builder.toString();
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

}
