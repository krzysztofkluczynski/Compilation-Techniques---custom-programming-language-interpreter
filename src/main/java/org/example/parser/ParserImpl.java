package org.example.parser;

import org.example.lexer.Lexer;
import org.example.lexer.error.*;
import org.example.parser.Structure.*;
import org.example.parser.Structure.utils.Type;
import org.example.token.Position;
import org.example.token.Token;
import org.example.token.TokenType;

import java.util.*;
import java.io.IOException;

public class ParserImpl implements Parser {
    Lexer lexer;
    Token token;
    private boolean parse = true;

    ParserImpl(Lexer lexer) {
        this.lexer = lexer;
    }

    private void nextToken() throws ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IntMaxValueExceededException, IOException, IdentifierTooLongException, DecimalMaxValueExceededException {
        token = lexer.next();
    }


    /*
    program = {function_definition}
    */
    public Program parseProgram() throws Exception {
        Map<String, FunctionDefinition> functionDefinitions = new HashMap<>();
        Optional<FunctionDefinition> funDefOptional;
        while ((funDefOptional = parseFunctionDefinition()).isPresent()) {
            FunctionDefinition funDef = funDefOptional.get();
            if (functionDefinitions.containsKey(funDef.getName())) {
                throw new Exception("Duplicate function definition: " + funDef.getName()); // TODO use a custom exception here.
            }
            functionDefinitions.put(funDef.getName(), funDef);
        }
        return new Program(functionDefinitions);
    }


    /*
     function_definition = "fn",  (type | "void"), identifier, "(", parameters-list, ")", block;
    */
    private Optional<FunctionDefinition> parseFunctionDefinition() throws Exception {
        if (!checkToken(TokenType.FUNCTION)) {
            return Optional.empty();
        }

        EnumSet<TokenType> allowedTypes = EnumSet.of(TokenType.INTEGER, TokenType.FLOAT, TokenType.BOOL, TokenType.STRING, TokenType.DICTIONARY, TokenType.LIST, TokenType.TUPLE, TokenType.VOID);
        proceedAndCheck(allowedTypes);
        TypeDeclaration typeDeclaration;
        if (token.getType() == TokenType.VOID) {
            typeDeclaration = null; //TODO, przemyslec czy ten null ro dobry pomysl?
        } else {
            typeDeclaration = parseType();
        }

        Position position = token.getPosition();


        proceedAndCheck(TokenType.IDENTIFIER);
        String name = token.getValue();

        proceedAndCheck(TokenType.BRACKET_OPEN);

        nextToken();
        List<Argument> arguments = parseArguments();

        proceedAndCheck(TokenType.BRACKET_CLOSE);


        nextToken();
        BlockStatement blockStatement = parseBlock();


        return Optional.of(new FunctionDefinition(typeDeclaration, name, arguments, blockStatement, position));
    }


    /*
    type = type_basic | type_complex
    */
    private TypeDeclaration parseType() throws Exception {
        TokenType tokenType = token.getType();
        if (tokenType == TokenType.INTEGER || tokenType == TokenType.FLOAT || tokenType == TokenType.STRING || tokenType == TokenType.BOOL ) {
            TypeDeclaration typeDeclaration = parseBasicType();
            return typeDeclaration;
        } else {
            TypeDeclaration typeDeclaration = parseComplexType();
            return typeDeclaration;

        }

    }

    private TypeDeclaration parseBasicType() {
        return new TypeDeclaration(Type.getTypeByName(token.getValue()), token.getPosition());
    }

    private TypeDeclaration parseComplexType() throws Exception {
        TokenType rememberedType = token.getType();
        Position rememberedPosition = token.getPosition();
        proceedAndCheck(TokenType.LESS);
        Type firstOptionalparameter;
        Type secondOptionalParameter;

        TypeDeclaration typeDeclaration;

        switch (rememberedType) { //TODO, add custom exceptions
            case LIST:
                nextToken();
                firstOptionalparameter = Type.getTypeByName(token.getValue());
                typeDeclaration = new TypeDeclaration(Type.LIST, firstOptionalparameter, rememberedPosition);
                break;
            case DICTIONARY:
                nextToken();
                firstOptionalparameter = Type.getTypeByName(token.getValue());
                proceedAndCheck(TokenType.COMMA);

                nextToken();
                secondOptionalParameter = Type.getTypeByName(token.getValue());
                typeDeclaration = new TypeDeclaration(Type.DICTIONARY, firstOptionalparameter, secondOptionalParameter, rememberedPosition);
                break;
            case TUPLE:
                nextToken();
                firstOptionalparameter = Type.getTypeByName(token.getValue());
                proceedAndCheck(TokenType.COMMA);

                nextToken();
                secondOptionalParameter = Type.getTypeByName(token.getValue());
                typeDeclaration = new TypeDeclaration(Type.TUPLE, firstOptionalparameter, secondOptionalParameter, rememberedPosition);
                break;
            default:
                throw new Exception("Error while parsing complex type");
        }
        proceedAndCheck(TokenType.GREATER);
        nextToken();
        return typeDeclaration;

    }


    private List<Argument>  parseArguments() {

    }

    private BlockStatement parseBlock() {
    }

    private boolean checkToken(TokenType tokenType) {
        if (token.getType() != tokenType) {
            return false;
        }
        return true;
    }

    private boolean checkToken(Collection<TokenType> tokenTypes) throws ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IntMaxValueExceededException, IOException, IdentifierTooLongException, DecimalMaxValueExceededException {
        if (!tokenTypes.contains(token.getType())) {
            return false;
        }
        return true;
    }

    private void proceedAndCheck(TokenType expected) throws Exception {
        nextToken();
        if (!checkToken(expected)) {
            throw new Exception("test"); //TODO, custom exceptions
        }

    }

    private void proceedAndCheck(Collection<TokenType> expected) throws Exception {
        nextToken();
        if (!checkToken(expected)) {
            throw new Exception("test"); //TODO, custom exceptions
        }


    }

}
