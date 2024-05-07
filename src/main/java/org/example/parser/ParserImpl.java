package org.example.parser;

import org.example.lexer.Lexer;
import org.example.lexer.error.*;
import org.example.parser.Structure.Expression.Expression;
import org.example.parser.Structure.OtherComponents.*;
import org.example.parser.Structure.Statement.*;
import org.example.parser.Enum.Type;
import org.example.token.Position;
import org.example.token.Token;
import org.example.token.TokenType;

import java.util.*;
import java.io.IOException;

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
                throw new Exception("Duplicate function definition: " + funDef.getName()); // It's a good idea to define a custom exception for clarity.
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

        EnumSet<TokenType> allowedTypes = EnumSet.of(TokenType.INTEGER, TokenType.FLOAT, TokenType.BOOL, TokenType.STRING, TokenType.DICTIONARY, TokenType.LIST, TokenType.TUPLE, TokenType.VOID);
        proceedAndCheck(allowedTypes);
        TypeDeclaration typeDeclaration = null;
        if (token.getType() != TokenType.VOID) {
            typeDeclaration = parseType(); // null represents void here.
        }

        Position position = token.getPosition();

        proceedAndCheck(TokenType.IDENTIFIER);
        String name = token.getValue();

        nextToken();
        List<Argument> parameters = parseParameters();

        nextToken();
        BlockStatement blockStatement = parseBlock(); // consider handling empty block case inside parseBlock

        return new FunctionDefinition(typeDeclaration, name, parameters, blockStatement, position);
    }



    /*
    type = type_basic | type_complex
    */
    private TypeDeclaration parseType() throws Exception {
        TokenType tokenType = token.getType();
        if (tokenType == TokenType.INTEGER || tokenType == TokenType.FLOAT || tokenType == TokenType.STRING || tokenType == TokenType.BOOL ) {
            return parseBasicType();
        } else if (tokenType == TokenType.LIST || tokenType == TokenType.TUPLE || tokenType == TokenType.DICTIONARY) {
            return parseComplexType();
        } else {
            throw new Exception("Couldn't find any of the langauge definded types");
        }
    }

    /*
     type_basic = "int" | "float" | "String" | "boolean";
     */
    private TypeDeclaration parseBasicType() {
        return new TypeDeclaration(Type.getTypeByName(token.getValue()), token.getPosition());
    }

    /*
 type_complex               = dictionary_declaration | tuple_declaration | list_declaration
 dictionary_declaration     = "Dictionary", "<", type_basic, ",", type_basic, ">";
 tuple_declaration          = "Tuple", "<", type_basic, ",", type_basic, ">";
 list_declaration           = "List", "<", type_basic, ">" ;
     */
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
                firstOptionalparameter = Type.getTypeByName(token.getValue()); //TODO, check if token is basic_type for each one
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
        return typeDeclaration;

    }
/*
    parameters-list = [ type, identifier, { ",", type,  identifier } ];
*/
    private List<Argument> parseParameters() throws Exception {
        List<Argument> arguments = new ArrayList<>();
        if(!checkToken(TokenType.BRACKET_OPEN)) {
            throw new Exception("expected (");
        }
        nextToken();
        if (token.getType() == TokenType.BRACKET_CLOSE) {
            return arguments;
        }

        while (true) { //TODO, przemyslec ta petle aby pozbyc sie while(true)
            TypeDeclaration type = parseType();
            proceedAndCheck(TokenType.IDENTIFIER);
            String name =token.getValue();
            Argument argument = new Argument(name, type, type.getPosition());
            arguments.add(argument);
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
        return arguments;
    }

    /*
    block                    = "{", { statement }, "}";
    statement                = conditional
                            | while_loop
                            | for_loop
                            | declaration_or_assignment
                            | function_call
                            | return_statement;

    */

    private BlockStatement parseBlock() throws Exception {
        List<Node> instructions = new ArrayList<>();
        proceedAndCheck(TokenType.CURLY_BRACKET_OPEN);
        Position position = token.getPosition();
        nextToken();
        if (token.getType() == TokenType.CURLY_BRACKET_CLOSE) {
            return new BlockStatement(instructions, position);
        }

        EnumSet<TokenType> allowedTypes = EnumSet.of( //TODO, lista przyda sie pozniej aby do wyjatku przekazac jakie tokeny sa obslugiwane
                //conditional
                TokenType.IF,
                //while loop
                TokenType.WHILE,
                //for loop
                TokenType.FOR,
                //declaration
                TokenType.INTEGER,
                TokenType.FLOAT,
                TokenType.BOOL,
                TokenType.STRING,
                TokenType.DICTIONARY,
                TokenType.LIST,
                TokenType.TUPLE,
                //assignment or function call
                TokenType.IDENTIFIER,
                //return statement
                TokenType.RETURN
        );

        Node node = null;

        while(token.getType() != TokenType.CURLY_BRACKET_CLOSE) {
            switch (token.getType()) {
                case IF -> node = parseConditionalStatement();
                case WHILE -> node = parseWhileStatement();
                case FOR -> node = parseForStatement();
                case IDENTIFIER -> node = parseStatementStartingWithIdentifier(); //function call or assignment
                case RETURN -> node = parseReturnStatement();
                case INTEGER, FLOAT, BOOL, STRING, LIST, TUPLE, DICTIONARY -> node = parseAssignment();
            }
            proceedAndCheck(TokenType.SEMICOLON);
            if (node == null) {
                throw new Exception("unexpected instruction or missing closnig bracket"); //TODO, custom exception
            }
            instructions.add(node);
            nextToken();
        }

        return new BlockStatement(instructions, position);
    }



/*
 conditional                = "if", "(", expression, ")", block,
                            [ { "elif", "(", expression, ")", block },
                            "else", block ];
 */

    private ConditionalStatement parseConditionalStatement() throws Exception {
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
        Position position = token.getPosition(); //taking position of while token

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

 */
    private Node parseStatementStartingWithIdentifier() throws Exception {
        String name = token.getValue();
        Position position = token.getPosition();
        nextToken();
        TokenType type = token.getType();

        if(type == TokenType.BRACKET_OPEN) {
            return parseFunctionCall();
        } else if ( type == TokenType.EQUAL) {
            return parseAssignment();
        } else {
            throw new Exception("Unexpected token");
        }

    }


/*
    function_call              = identifier, "(", arguments-list, ")", ";";

 */
    private FunctionCall parseFunctionCall() {

    }

/*
    arguments-list             = [ expression, { ",", expression } ];
*/
    private List<Expression> parseArguments() {

    }


    private Node parseAssignment() {
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
