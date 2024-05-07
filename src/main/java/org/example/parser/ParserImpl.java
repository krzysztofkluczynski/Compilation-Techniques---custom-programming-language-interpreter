package org.example.parser;

import org.example.lexer.Lexer;
import org.example.lexer.error.*;
import org.example.parser.Error.DuplicateIdentiferException;
import org.example.parser.Error.ParsingException;
import org.example.parser.Structure.Expression.Expression;
import org.example.parser.Structure.OtherComponents.*;
import org.example.parser.Structure.Statement.*;
import org.example.parser.Enum.Type;
import org.example.token.Position;
import org.example.token.Token;
import org.example.token.TokenType;

import java.util.*;
import java.io.IOException;
import java.util.function.Supplier;

public class ParserImpl implements Parser {
    Lexer lexer;
    Token token;

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
        FunctionDefinition funDef;
        while ((funDef = parseFunctionDefinition()) != null) {
            if (functionDefinitions.containsKey(funDef.getName())) {
                throw new DuplicateIdentiferException(funDef.getPosition(), funDef.getName());
            }
            functionDefinitions.put(funDef.getName(), funDef);
        }
        return new Program(functionDefinitions);
    }

    /*
     function_definition = "fn",  (type | "void"), identifier, "(", parameters-list, ")", block;
    */
    private FunctionDefinition parseFunctionDefinition() throws Exception {
        if (!checkToken(TokenType.FUNCTION)) {
            return null;
        }

        TypeDeclaration typeDeclaration = parseType();

        Position position = token.getPosition();

        proceedAndCheck(TokenType.IDENTIFIER);
        String name = token.getValue();


        nextToken();
        List<Argument> parameters = parseParameters(); // brackets read inside parseParameters

        nextToken();
        BlockStatement blockStatement = parseBlock(); // TODO, handle empty block statement?

        return new FunctionDefinition(typeDeclaration, name, parameters, blockStatement, position);
    }



    /*
    type = type_basic | type_complex
    */
    private TypeDeclaration parseType() throws Exception {
        EnumSet<TokenType> allowedTypes = EnumSet.of(TokenType.INTEGER, TokenType.FLOAT, TokenType.BOOL, TokenType.STRING, TokenType.DICTIONARY, TokenType.LIST, TokenType.TUPLE, TokenType.VOID);
        proceedAndCheck(allowedTypes);

        TokenType tokenType = token.getType();
        if (tokenType == TokenType.INTEGER || tokenType == TokenType.FLOAT || tokenType == TokenType.STRING || tokenType == TokenType.BOOL ) {
            return parseBasicType();
        } else if (tokenType == TokenType.LIST || tokenType == TokenType.TUPLE || tokenType == TokenType.DICTIONARY) {
            return parseComplexType();
        }

        return null;
    }

    /*
     type_basic = "int" | "float" | "String" | "boolean";
     */
    private TypeDeclaration parseBasicType() {
        return new TypeDeclaration(Type.getTypeByName(token.getValue()), token.getPosition());
    }

    /*
    type_complex               = dictionary_declaration | tuple_declaration | list_declaration
     */
    private TypeDeclaration parseComplexType() throws Exception {
        TokenType rememberedType = token.getType();
        Position rememberedPosition = token.getPosition();
        Type firstOptionalparameter;
        Type secondOptionalParameter;

        TypeDeclaration typeDeclaration;

        if (token.getType() == TokenType.LIST) {
            return parseList();
        } else if (token.getType() == TokenType.DICTIONARY) {
            return parseDictionary();
        } else if (token.getType() == TokenType.TUPLE) {
            return parseTuple();
        } else {
            List<TokenType> allowedTypes = List.of(TokenType.TUPLE, TokenType.LIST, TokenType.DICTIONARY);
            throw new ParsingException(rememberedPosition, allowedTypes, token.getType());
        }

    }`


    /*
    list_declaration           = "List", "<", type_basic, ">" ;
     */
    private TypeDeclaration parseList() throws Exception {
        Position position = token.getPosition();
        proceedAndCheck(TokenType.LESS);
        nextToken();
        Type type = Type.getTypeByName(token.getValue());
        proceedAndCheck(TokenType.GREATER);
        return new TypeDeclaration(Type.LIST, type, position);
    }

    /*
    dictionary_declaration     = "Dictionary", "<", type_basic, ",", type_basic, ">";
    */
    private TypeDeclaration parseDictionary() throws Exception {
        Position position = token.getPosition();
        proceedAndCheck(TokenType.LESS);
        nextToken();
        Type typeOne = Type.getTypeByName(token.getValue());
        proceedAndCheck(TokenType.COMMA);
        nextToken();
        Type typeTwo = Type.getTypeByName(token.getValue());
        proceedAndCheck(TokenType.GREATER);
        return new TypeDeclaration(Type.DICTIONARY, typeOne, typeTwo, position);
    }

    /*
    tuple_declaration          = "Tuple", "<", type_basic, ",", type_basic, ">";
     */
    private TypeDeclaration parseTuple() throws Exception {
        Position position = token.getPosition();
        proceedAndCheck(TokenType.LESS);
        nextToken();
        Type typeOne = Type.getTypeByName(token.getValue());
        proceedAndCheck(TokenType.COMMA);
        nextToken();
        Type typeTwo = Type.getTypeByName(token.getValue());
        proceedAndCheck(TokenType.GREATER);
        return new TypeDeclaration(Type.TUPLE, typeOne, typeTwo, position);
    }


    /*
    parameters-list = [ type, identifier, { ",", type,  identifier } ];
    */
    private List<Argument> parseParameters() throws Exception {
        List<Argument> arguments = new ArrayList<>();

        proceedAndCheck(TokenType.BRACKET_OPEN);


        while (token.getType() != TokenType.BRACKET_CLOSE) {
            TypeDeclaration type = parseType();
            proceedAndCheck(TokenType.IDENTIFIER);
            String name = token.getValue();
            Argument argument = new Argument(name, type, type.getPosition());
            arguments.add(argument);

            proceedAndCheck(TokenType.COMMA);
            nextToken();
            }
        return arguments;

        }

    /*
    block                    = "{", { statement }, "}";
    statement                  = conditional
                            | while_loop
                            | for_loop
                            | declaration_or_definition
                            | function_call_or_assignment
                            | return_statement;
                            | expression;
    */

    private BlockStatement parseBlock() throws Exception {
        List<Statement> instructions = new ArrayList<>();
        if(!checkToken(TokenType.CURLY_BRACKET_OPEN)) {
            throw new ParsingException(token.getPosition(), TokenType.CURLY_BRACKET_OPEN, token.getType());
        }
        Position position = token.getPosition();
        nextToken();

        // Define a list of parser functions
        List<Supplier<Statement>> parsers = Arrays.asList(
                this::parseConditionalStatement,
                this::parseWhileStatement,
                this::parseForStatement,
                this::parseStatementStartingWithIdentifier,
                this::parseReturnStatement,
                this::parseDeclaration
        );


        while (token.getType() != TokenType.CURLY_BRACKET_CLOSE) {
            Statement statement = null;
            for (Supplier<Statement> parser : parsers) {
                statement = parser.get();
                if (statement != null) {
                    instructions.add(statement);
                    break; // Exit the loop once a valid statement is parsed
                }
            }
            if (statement == null) {
                // If no statement is parsed, throw an exception or handle the error appropriately
                List<TokenType> allowedTypes = List.of(TokenType.IF, TokenType.WHILE); //TODO, uzupelnic typy tokenow
                throw new ParsingException(position, allowedTypes, token.getType());
            }
        }

        nextToken();

        return new BlockStatement(instructions, position);
    }


    /*
    conditional = "if", "(", expression, ")", block,
                [ { "elif", "(", expression, ")", block },
                "else", block ];
    */
    private ConditionalStatement parseConditionalStatement() throws Exception {
        if(checkToken(TokenType.IF)) {
            return null;
        }
        Position position = token.getPosition();
        proceedAndCheck(TokenType.BRACKET_OPEN);

        Expression ifExpression = parseExpression();

        BlockStatement ifBlock = parseBlock();
        nextToken();

        List<Elif> elifs = new ArrayList<>();

        while(token.getType() != TokenType.ELSE) {
            if (!checkToken(TokenType.ELIF)) {
                throw new Exception("expected elif token");
            }
            Position elifPosition = token.getPosition();
            proceedAndCheck(TokenType.BRACKET_OPEN);
            Expression elifExpression= parseExpression();
            proceedAndCheck(TokenType.BRACKET_CLOSE);

            nextToken();
            BlockStatement elifBlock = parseBlock();
            nextToken();

            Elif elif = new Elif(elifExpression, elifBlock, elifPosition);
            elifs.add(elif);
        }

        BlockStatement elseBlock = parseBlock();

        return new ConditionalStatement(ifExpression, ifBlock, elifs, elseBlock, position);
    }
    /*
    for_loop = "for", "(", type, identifier ":" ,identifier, ")", block;
    */
    private ForStatement parseForStatement() throws Exception {
        Position position = token.getPosition(); //taking position of for token

        proceedAndCheck(TokenType.BRACKET_OPEN);

        TypeDeclaration typeDeclaration = parseType();
        proceedAndCheck(TokenType.IDENTIFIER);
        String identiferName = token.getValue();

        proceedAndCheck(TokenType.COLON);

        proceedAndCheck(TokenType.IDENTIFIER);
        String collectionName = token.getValue();

        proceedAndCheck(TokenType.BRACKET_CLOSE);

        nextToken();
        BlockStatement blockStatement = parseBlock();


        return new ForStatement(typeDeclaration, identiferName, collectionName, blockStatement, position);
    }

    /*
    while_loop = "while", "(", expression, ")", block;
    */
    private WhileStatement parseWhileStatement() throws Exception {
        Position position = token.getPosition(); //taking position of while token

        proceedAndCheck(TokenType.BRACKET_OPEN);

        Expression expression = parseExpression();

        proceedAndCheck(TokenType.BRACKET_CLOSE);

        nextToken();
        BlockStatement blockStatement = parseBlock();

        return new WhileStatement(expression, blockStatement, position);
    }

    /*
    declaration_or_assignment  = [type], identifier, ["=", (expression | query_statement)], ";";
    function_call = identifier, "(", arguments-list, ")", ";";
    */
    private Node parseStatementStartingWithIdentifier() throws Exception {
        String name = token.getValue();
        Position position = token.getPosition();
        nextToken();
        TokenType type = token.getType();

        if(type == TokenType.BRACKET_OPEN) {
            return parseFunctionCall(name, position);
        } else if ( type == TokenType.EQUAL) {
            return parseAssignment();
        } else {
            throw new Exception("Unexpected token");
        }

    }

    /*
    function_call = identifier, "(", arguments-list, ")", ";";
    */
    private FunctionCall parseFunctionCall(String name, Position position) throws Exception {
        nextToken();
        List<Expression> arguments = parseArguments();
        proceedAndCheck(TokenType.SEMICOLON);
        return new FunctionCall(name, arguments, position);

    }

    /*
    arguments-list = [ expression, { ",", expression } ];
    */
    private List<Expression> parseArguments() throws Exception {
        List<Expression> expressions = new ArrayList<>();
        if (checkToken(TokenType.BRACKET_CLOSE)) {
            return expressions;
        }
        while (true) { //TODO, przemyslec ta petle aby pozbyc sie while(true)
            Expression expression = parseExpression();
            expressions.add(expression);
            nextToken();

            if (token.getType() == TokenType.BRACKET_CLOSE) {
                break;
            }
            if (checkToken(TokenType.COMMA)) {
                nextToken();
                //continue;
            } else {
                throw new Exception("Comma is expected");
            }

        }

        return expressions;
    }


    private Node parseAssignment() {

    }

    private Node parseDeclaration() {

    }

    private Node parseReturnStatement() {

    }



    private Expression parseExpression() {

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
            throw new ParsingException(token.getPosition(), expected, token.getType()); //TODO, custom exceptions
        }

    }

    private void proceedAndCheck(Collection<TokenType> expected) throws Exception {
        nextToken();
        if (!checkToken(expected)) {
            throw new ParsingException(token.getPosition(), (List<TokenType>) expected, token.getType());
        }


    }

}
