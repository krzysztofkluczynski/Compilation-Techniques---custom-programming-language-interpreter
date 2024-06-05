package org.example.parser;

import org.example.lexer.Lexer;
import org.example.lexer.error.*;
import org.example.parser.Enum.*;
import org.example.parser.Error.DuplicateIdentiferException;
import org.example.parser.Error.ParsingException;
import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.*;
import org.example.parser.Structure.OtherComponents.*;
import org.example.parser.Structure.Statement.*;
import org.example.token.Position;
import org.example.token.Token;
import org.example.token.TokenType;

import java.util.*;
import java.io.IOException;

public class ParserImpl implements Parser {
    Lexer lexer;
    Token token;

    public ParserImpl(Lexer lexer) throws ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IntMaxValueExceededException, IOException, IdentifierTooLongException, DecimalMaxValueExceededException {
        this.lexer = lexer;
        nextToken();
    }

    private void nextToken() throws ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IntMaxValueExceededException, IOException, IdentifierTooLongException, DecimalMaxValueExceededException {
        token = lexer.next();
        if (token.getType().equals(TokenType.ONE_LINE_COMMENT) || token.getType().equals(TokenType.MULTI_LINE_COMMENT)) {
            nextToken();
        }
    }

    /*
    program = {function_definition}
    */
    public Program parseProgram() throws Exception {
        Map<String, FunctionDefinition> functionDefinitions = new HashMap<>();
        FunctionDefinition funDef;
        while ((token.getType() != TokenType.END_OF_FILE)) {
            funDef = parseFunctionDefinition();
            if (funDef == null) {
                throw new ParsingException(token.getPosition(), TokenType.FUNCTION, token.getType());
            }
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
        Position position = token.getPosition();

        nextToken();
        TypeDeclaration typeDeclaration = parseType();



        proceedAndCheck(TokenType.IDENTIFIER);
        String name = token.getValue();


        proceedAndCheck(TokenType.BRACKET_OPEN);
        List<Argument> parameters = parseParameters(); // closing read inside parseParameters

        nextToken();
        BlockStatement blockStatement = parseBlock(); // TODO, handle empty block statement?

        return new FunctionDefinition(typeDeclaration, name, parameters, blockStatement, position);
    }


    /*
    type = type_basic | type_complex
    */
    private TypeDeclaration parseType() throws Exception {
        List<TokenType> allowedTypes = List.of(TokenType.INTEGER, TokenType.FLOAT, TokenType.BOOL, TokenType.STRING, TokenType.DICTIONARY, TokenType.LIST, TokenType.TUPLE, TokenType.VOID);
        if(!checkToken(allowedTypes)) {
            throw new ParsingException(token.getPosition(), allowedTypes, token.getType());
        }

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

    }


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


        while (token.getType() != TokenType.BRACKET_CLOSE) {
            nextToken();
            if(checkToken(TokenType.BRACKET_CLOSE)) {
                break;
            }
            TypeDeclaration type = parseType();
            proceedAndCheck(TokenType.IDENTIFIER);
            String name = token.getValue();
            Argument argument = new Argument(name, type, type.getPosition());
            arguments.add(argument);


            List<TokenType> list = List.of(TokenType.COMMA, TokenType.BRACKET_CLOSE);
            proceedAndCheck(list);
            }
        return arguments;

        }

    /*
    block                    = "{", { statement }, "}";
    */

    public BlockStatement parseBlock() throws Exception {
        if (!checkToken(TokenType.CURLY_BRACKET_OPEN)) {
            throw new ParsingException(token.getPosition(), TokenType.CURLY_BRACKET_OPEN, token.getType());
        }
        Position blockStartPosition = token.getPosition();
        nextToken();

        List<Statement> statements = parseStatements(blockStartPosition); //reads curly bracket at the end

        nextToken();  // Move past the closing curly bracket

        return new BlockStatement(statements, blockStartPosition);
    }

    /*
    statement                  = conditional
                            | while_loop
                            | for_loop
                            | declaration_or_definition
                            | function_call_or_assignment
                            | return_statement;
                            | expression;

     */
    private List<Statement> parseStatements(Position position) throws Exception {
        List<Statement> statements = new ArrayList<>();

        while (token.getType() != TokenType.CURLY_BRACKET_CLOSE) {
            Statement statement = tryParseStatement();
            if (statement != null) {
                statements.add(statement);
            }
            else {
                List<TokenType> allowedTypes = List.of(TokenType.IF,
                        TokenType.WHILE,
                        TokenType.FOR,
                        TokenType.IDENTIFIER,
                        TokenType.INTEGER,
                        TokenType.STRING,
                        TokenType.BOOL,
                        TokenType.FLOAT,
                        TokenType.LIST,
                        TokenType.DICTIONARY,
                        TokenType.TUPLE,
                        TokenType.RETURN);

                throw new ParsingException(position, allowedTypes, token.getType());
            }
        }

        return statements;
    }

    private Statement tryParseStatement() throws Exception {    Statement statement;

        // Try parsing using each parsing method one by one
        statement = parseConditionalStatement();
        if (statement != null) return statement;

        statement = parseWhileStatement();
        if (statement != null) return statement;

        statement = parseForStatement();
        if (statement != null) return statement;

        statement = (Statement) parseStatementStartingWithIdentifier(); //FUNCTION CALL OR ASSIGNMENT
        if (statement != null) return statement;

        statement = parseStatementStartingWithType(); //DECLARATION OR DEFINITION
        if (statement != null) return statement;

        statement = parseReturnStatement();
        if (statement != null) return statement;


        return null;
    }


    /*
    conditional = "if", "(", expression, ")", block,
                [ { "elif", "(", expression, ")", block },
                "else", block ];
    */
    private ConditionalStatement parseConditionalStatement() throws Exception {
        if(!checkToken(TokenType.IF)) {
            return null;
        }
        Position position = token.getPosition();
        proceedAndCheck(TokenType.BRACKET_OPEN);
        nextToken();

        IExpression ifExpression = parseExpression();

        if(!checkToken(TokenType.BRACKET_CLOSE)) {
            throw new ParsingException(token.getPosition(), TokenType.BRACKET_CLOSE, token.getType());
        }

        nextToken();
        BlockStatement ifBlock = parseBlock();
        If ifStatement = new If(ifExpression, ifBlock, ifExpression.getPosition());


        List<If> ifs = new ArrayList<>();
        ifs.add(ifStatement);

        if(!checkToken(TokenType.ELSE) && !checkToken(TokenType.ELIF)) {
            return new ConditionalStatement(ifs, ifStatement.getPosition());
        }


        while(token.getType() != TokenType.ELSE) {
            if (!checkToken(TokenType.ELIF)) {
                throw new ParsingException(token.getPosition(), List.of(TokenType.ELIF, TokenType.ELSE), token.getType());
            }
            Position elifPosition = token.getPosition();
            proceedAndCheck(TokenType.BRACKET_OPEN);
            nextToken();
            IExpression elifExpression= parseExpression();

            nextToken();
            BlockStatement elifBlock = parseBlock();


            If elif = new If(elifExpression, elifBlock, elifPosition);
            ifs.add(elif);
        }

        nextToken();
        BlockStatement elseBlock = parseBlock();
        ifs.add(new If(null, elseBlock, elseBlock.getPosition()));


        return new ConditionalStatement(ifs, position);
    }
    /*
    for_loop = "for", "(", type, identifier ":" ,identifier, ")", block;
    */
    private ForStatement parseForStatement() throws Exception {
        if(!checkToken(TokenType.FOR)) {
            return null;
        }
        Position position = token.getPosition(); //taking position of for token

        proceedAndCheck(TokenType.BRACKET_OPEN);

        nextToken();
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
        if(!checkToken(TokenType.WHILE)) {
            return null;
        }
        Position position = token.getPosition(); //taking position of while token

        proceedAndCheck(TokenType.BRACKET_OPEN);
        nextToken();

        IExpression expression = parseExpression();

        if(!checkToken(TokenType.BRACKET_CLOSE)) {
            throw new ParsingException(token.getPosition(), TokenType.BRACKET_CLOSE, token.getType());
        }

        nextToken();
        BlockStatement blockStatement = parseBlock();

        return new WhileStatement(expression, blockStatement, position);
    }

    /*
 function_call_or_assignment = identifier, (, "(", arguments-list, ")", | ["=", (expression ),  ";";
    */
    private Node parseStatementStartingWithIdentifier() throws Exception {
        if(!checkToken(TokenType.IDENTIFIER)) {
            return null;
        }
        String name = token.getValue();
        Position identifierPosition = token.getPosition();
        nextToken();

        if(token.getType() == TokenType.BRACKET_OPEN) {
            FunctionCall functionCall = parseFunctionCall(name, identifierPosition);
            nextToken();
            return functionCall;
        } else if (token.getType() == TokenType.EQUAL) {
            return parseAssignment(name, identifierPosition);
        } else if (checkToken(TokenType.DOT)) {
            nextToken();
            IExpression methodCall = parseMethodCallOrLambda(name);
            nextToken();
            return methodCall;
        }

        else {
            List<TokenType> allowedTypes = List.of(TokenType.BRACKET_OPEN, TokenType.EQUAL);
            throw new ParsingException(identifierPosition, allowedTypes, token.getType());
        }

    }

    private Statement parseAssignment(String name, Position identifierPosition) throws Exception {
        nextToken();
        IExpression expression = parseExpression();
        Statement statement;
        if (expression == null) {
            QueryExpression queryExpression = parseQueryStatement();
            if (queryExpression == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IF,
                        TokenType.WHILE,
                        TokenType.FOR,
                        TokenType.IDENTIFIER,
                        TokenType.INT_LITERAL,
                        TokenType.STRING_LITERAL,
                        TokenType.BOOL_LITERAL,
                        TokenType.FLOAT_LITERAL,
                        TokenType.PIPE,
                        TokenType.BRACKET_OPEN,
                        TokenType.SQUARE_BRACKET_OPEN
                        );
                throw new ParsingException(identifierPosition, allowedTypes, token.getType());
            } else {
                if(!checkToken(TokenType.SEMICOLON)) {
                    throw new ParsingException(token.getPosition(), TokenType.SEMICOLON, token.getType());
                }
                return new AssignmentWithQueryStatement(name, queryExpression, identifierPosition);
            }

        }
        if(!checkToken(TokenType.SEMICOLON)) {
            throw new ParsingException(token.getPosition(), TokenType.SEMICOLON, token.getType());
        }
        nextToken();
        return new AssignmentWithExpressionStatement(name, expression, identifierPosition);

    }

    /*
    function_call = identifier, "(", arguments-list, ")", ";";
    */
    private FunctionCall parseFunctionCall(String name, Position position) throws Exception {
        nextToken();
        List<IExpression> arguments = parseArguments();
        proceedAndCheck(TokenType.SEMICOLON);
        return new FunctionCall(name, arguments, position);
    }

    /*
    arguments-list = [ expression, { ",", expression } ];
    */
    private List<IExpression> parseArguments() throws Exception {
        List<IExpression> expressions = new ArrayList<>();

        // Immediately return if the next token is a closing bracket.
        if (checkToken(TokenType.BRACKET_CLOSE)) {
            return expressions;
        }


        // Loop until the closing bracket is found.
        while (token.getType() != TokenType.BRACKET_CLOSE) {
            IExpression expression = parseExpression();
            expressions.add(expression);

            // Advance to the next token to check if it's a comma or a closing bracket.
            //nextToken();

            if (token.getType() == TokenType.BRACKET_CLOSE) {
                break;
            } else if (checkToken(TokenType.COMMA)) {
                nextToken();  // Move past the comma to the next expression.
            } else {
                throw new ParsingException(token.getPosition(), TokenType.COMMA, token.getType());
            }
        }

        return expressions;
    }

    /*
     declaration_or_definition  = type, identifier, ["=", expression], ";";
     */
    private Statement parseStatementStartingWithType() throws Exception {
        List<TokenType> allowedTypes = List.of(
                TokenType.INTEGER,
                TokenType.FLOAT,
                TokenType.BOOL,
                TokenType.STRING,
                TokenType.LIST,
                TokenType.TUPLE,
                TokenType.DICTIONARY
        );
        if(!checkToken(allowedTypes)) {
            return null;
        }
        TypeDeclaration type = parseType();

        nextToken();
        if(!checkToken(TokenType.IDENTIFIER)) {
            throw new ParsingException(token.getPosition(), TokenType.IDENTIFIER, token.getType());
        }
        String name = token.getValue();

        nextToken();

        if(checkToken(TokenType.SEMICOLON)) {
            nextToken();
            return new DeclarationStatement(type, name, type.getPosition());
        } else if (checkToken(TokenType.EQUAL)) {
            return parseDefinition(type, name, type.getPosition());
        } else {
            List<TokenType> allowedTypesAfterIdentifier = List.of(TokenType.SEMICOLON, TokenType.EQUAL);
            throw new ParsingException(type.getPosition(), allowedTypesAfterIdentifier, token.getType());
        }
    }

    private Statement parseDefinition(TypeDeclaration type,String name, Position identifierPosition) throws Exception {
        nextToken();
        IExpression expression = parseExpression();
        Statement statement;
        if (expression == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IF,
                        TokenType.WHILE,
                        TokenType.FOR,
                        TokenType.IDENTIFIER,
                        TokenType.INT_LITERAL,
                        TokenType.STRING_LITERAL,
                        TokenType.BOOL_LITERAL,
                        TokenType.FLOAT_LITERAL
                ); //TODO, review those types
                throw new ParsingException(identifierPosition, allowedTypes, token.getType());
            }

        if(!checkToken(TokenType.SEMICOLON)) {
            throw new ParsingException(token.getPosition(), TokenType.SEMICOLON, token.getType());
        }
        nextToken();
        return new DefinitionWithExpressionStatement(type, name, expression, identifierPosition);

    }

    /*
    "return", [ expression ], ";";
     */
    private Statement parseReturnStatement() throws Exception {
        if(!checkToken(TokenType.RETURN)) {
            return null;
        }
        Position position = token.getPosition();

        nextToken();
        IExpression expression = parseExpression();
        if (!checkToken(TokenType.SEMICOLON)) {
            throw new ParsingException(token.getPosition(), TokenType.SEMICOLON, token.getType());
        }
        nextToken();
        return new ReturnStatement(expression, position);

    }


    /*
    expression = conjunction, { "or", conjunction };
    */
    private IExpression parseExpression() throws Exception {
        return parseOrExpression();
    }

    /*
    expression = conjunction, { "or", conjunction };
    */
    private IExpression parseOrExpression() throws Exception {
        IExpression left = parseAndExpression();
        if (left == null) {
            return null;
        }
        while(token.getType() == TokenType.OR) {
            nextToken();
            IExpression right = parseAndExpression();
            if (right == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IF,
                        TokenType.WHILE,
                        TokenType.FOR,
                        TokenType.IDENTIFIER,
                        TokenType.INT_LITERAL,
                        TokenType.STRING_LITERAL,
                        TokenType.BOOL_LITERAL,
                        TokenType.FLOAT_LITERAL
                ); //TODO, review those types
                throw new ParsingException(token.getPosition(), allowedTypes, token.getType());
            }
            left = new OrExpression(left, right, left.getPosition());
        }
        return left;
    }
    /*
    conjunction                = relation_term, { "and", relation_term };
    */
    private IExpression parseAndExpression() throws Exception {
        IExpression left = parseRelativeExpression();
        if (left == null) {
            return null;
        }
        while(token.getType() == TokenType.AND) {
            nextToken();
            IExpression right = parseRelativeExpression();
            if (right == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IF,
                        TokenType.WHILE,
                        TokenType.FOR,
                        TokenType.IDENTIFIER,
                        TokenType.INT_LITERAL,
                        TokenType.STRING_LITERAL,
                        TokenType.BOOL_LITERAL,
                        TokenType.FLOAT_LITERAL
                ); //TODO, review those types
                throw new ParsingException(token.getPosition(), allowedTypes, token.getType());
            }
            left = new AndExpression(left, right, left.getPosition());
        }
        return left;
    }
    /*
    relation_term = additive_term, [ relation_operator , additive_term ];
    */
    private IExpression parseRelativeExpression() throws Exception {
        IExpression left = parseArithmeticExpression();
        if (left == null) {
            return null;
        }
        RelativeType relativeType;
        if ((relativeType = RelativeType.getConditionOperandBySymbol(token.getValue())) != null) {
            nextToken();
            IExpression right = parseArithmeticExpression();
            if (right == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IF,
                        TokenType.WHILE,
                        TokenType.FOR,
                        TokenType.IDENTIFIER,
                        TokenType.INT_LITERAL,
                        TokenType.STRING_LITERAL,
                        TokenType.BOOL_LITERAL,
                        TokenType.FLOAT_LITERAL
                );  //TODO, change types
                throw new ParsingException(token.getPosition(), allowedTypes, token.getType());
            }
            left = new RelationExpression(left, relativeType, right, left.getPosition());
        }
        return left;
    }
    /*
    relation_term = additive_term, [ relation_operator , additive_term ];
    */
    private IExpression parseArithmeticExpression() throws Exception {
        IExpression left = parseMultiplicativeExpression();
        if (left == null) {
            return null;
        }
        AdditiveType additiveType;
        if ((additiveType = AdditiveType.getExpressionOperandBySymbol(token.getValue())) != null) {
            nextToken();
            IExpression right = parseMultiplicativeExpression();
            if (right == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IF,
                        TokenType.WHILE,
                        TokenType.FOR,
                        TokenType.IDENTIFIER,
                        TokenType.INT_LITERAL,
                        TokenType.STRING_LITERAL,
                        TokenType.BOOL_LITERAL,
                        TokenType.FLOAT_LITERAL
                ); //TODO, review those types
                throw new ParsingException(token.getPosition(), allowedTypes, token.getType());
            }
            left = new ArthmeticExpression(left, additiveType, right, left.getPosition());
        }
        return left;
    }
    /*
    multiplicative_term        = factor, { mul_div_operator, factor };
    */
    private IExpression parseMultiplicativeExpression() throws Exception {
        IExpression left = parseNegatedFactor();
        if (left == null) {
            return null;
        }
        MultiplicativeType multiplicativeType;
        while ((multiplicativeType = MultiplicativeType.getExpressionOperandBySymbol(token.getValue())) != null) {
            nextToken();
            IExpression right = parseNegatedFactor();
            if (right == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IF,
                        TokenType.WHILE,
                        TokenType.FOR,
                        TokenType.IDENTIFIER,
                        TokenType.INT_LITERAL,
                        TokenType.STRING_LITERAL,
                        TokenType.BOOL_LITERAL,
                        TokenType.FLOAT_LITERAL
                ); //TODO, review those types
                throw new ParsingException(token.getPosition(), allowedTypes, token.getType());
            }
            left = new MultiplicativeExpression(left,multiplicativeType, right, left.getPosition());
        }
        return left;
    }

    private IExpression parseNegatedFactor() throws Exception {
        boolean negated = false;
        Position position = token.getPosition();
        List<TokenType> allowedTypes = List.of(TokenType.NOT, TokenType.MINUS);
        if (checkToken(allowedTypes)) {
            negated = true;
            nextToken();
        }
        IExpression expression = parseFactor();
        if (negated && expression == null) {
            List<TokenType> allowedForException = List.of(TokenType.IF,
                    TokenType.WHILE,
                    TokenType.FOR,
                    TokenType.IDENTIFIER,
                    TokenType.INT_LITERAL,
                    TokenType.STRING_LITERAL,
                    TokenType.BOOL_LITERAL,
                    TokenType.FLOAT_LITERAL
            ); //TODO, review those types
            throw new ParsingException(token.getPosition(), allowedForException, token.getType());
        }
        if (negated) {
            return new NegatedExpression(expression, position);
        }
        return expression;
    }
    /*
 factor                     = ["not"],
                            | ["-"]
                            | literal
                            | identifier, [ ".", (function_call | identifier, "(" lambda_expression ")") ]
                            | cast_expression
                            | "(", expression, ")";
                            | queryStatement

     */
    private IExpression parseFactor() throws Exception {
        IExpression expression = parseLiteral();
        if (expression != null) {
            return expression;
        }

        expression = parseStartingwithIdentifier();
        if (expression != null) {
            return expression;
        }

        expression = parseCastExpression();
        if (expression != null) {
            return expression;
        }

        expression = parseQueryStatement();
        if (expression != null) {
            return expression;
        }

        //TODO, przemyslec dodanie tutaj nawiasu za i przed parseExpression, rowniez w samej gramatyce
        expression = parseExpression();
        if (expression != null) {
            return expression;
        }


        return expression;
    }


    /*
     literal                    = boolean | string | integer | float | complex_literal;
     complex_literal            = dictionary_literal | tuple_literal | list_literal
     */
    private IExpression parseLiteral() throws Exception {
        IExpression expression = null;
        Position position = token.getPosition();
        if (checkToken(TokenType.INT_LITERAL)) {
            expression = new LiteralInteger(token.getValue(), position);
        }
        if (checkToken(TokenType.FLOAT_LITERAL)) {
            expression = new LiteralFloat(token.getValue(),position);
        }
        if (checkToken(TokenType.BOOL_LITERAL)) {
            expression = new LiteralBool(token.getValue(), position);
        }
        if (checkToken(TokenType.STRING_LITERAL)) {
            expression = new LiteralString(token.getValue(), position);
        }
        if (checkToken(TokenType.SQUARE_BRACKET_OPEN)) {
            List<SimpleLiteral> literals = parseListLiteral();
            expression = new LiteralList(literals, position);
        }
        if (checkToken(TokenType.BRACKET_OPEN)) {
            expression = parseTupleLiteral();
        }
        if (checkToken(TokenType.PIPE)) {
            Map<SimpleLiteral, SimpleLiteral> map = parseDictionaryLiteral();
            expression = new LiteralDictionary(map, position);
        }
        if (expression == null) {
            return expression;
        }
        nextToken();
        return expression;
    }

    private Map<SimpleLiteral,SimpleLiteral> parseDictionaryLiteral() throws Exception {
        if(!checkToken(TokenType.PIPE)) {
            return null;
        }
        nextToken();
        SimpleLiteral objectOne;
        SimpleLiteral objectTwo;
        Map<SimpleLiteral, SimpleLiteral> map = new HashMap<>();
        
        while(!checkToken(TokenType.PIPE)) { //dictionary literal ends with Semicolon

            objectOne = (SimpleLiteral) parseLiteral();
            if(!checkToken(TokenType.COLON)) {
                throw new ParsingException(token.getPosition(), TokenType.COLON, token.getType());
            }
            nextToken();
            objectTwo = (SimpleLiteral) parseLiteral();
            
            map.put(objectOne, objectTwo);

            List<TokenType> list = List.of(TokenType.COMMA, TokenType.PIPE);
            if(!checkToken(list)) {
                throw new ParsingException(token.getPosition(), list, token.getType());
            }
            if (checkToken(TokenType.COMMA)) {
                nextToken();
            }

            
        }
        return map;
    }

    private IExpression parseTupleLiteral() throws Exception {
        if(!checkToken(TokenType.BRACKET_OPEN)) {
            return null;
        }
        nextToken();
        SimpleLiteral objectOne;
        SimpleLiteral objectTwo;

        objectOne = (SimpleLiteral) parseLiteral();
        if(!checkToken(TokenType.COMMA)) {
            throw new ParsingException(token.getPosition(), TokenType.COMMA, token.getType());
        }
            nextToken();
            objectTwo = (SimpleLiteral) parseLiteral();
            if(!checkToken(TokenType.BRACKET_CLOSE)) {
                throw new ParsingException(token.getPosition(), TokenType.BRACKET_CLOSE, token.getType());
            }
            return new LiteralTuple(objectOne, objectTwo, token.getPosition());
    }

    private List<SimpleLiteral> parseListLiteral() throws Exception {
        if(!checkToken(TokenType.SQUARE_BRACKET_OPEN)) {
            return null;
        }
        nextToken();
        List<SimpleLiteral> literals = new ArrayList<>();

        while(!checkToken(TokenType.SQUARE_BRACKET_CLOSE)) {
            IExpression literal = parseLiteral();

            literals.add((SimpleLiteral) literal);

            if (checkToken(TokenType.SQUARE_BRACKET_CLOSE))
            {
                break;
            }

            if(checkToken(TokenType.COMMA)) {
                nextToken();
                continue;
            }

        }

    return literals;
    }
/*
  | identifier, [ ".", (function_call | identifier, "(" lambda_expression ")") ]
 */
    private IExpression parseStartingwithIdentifier() throws Exception {
        if (!checkToken(TokenType.IDENTIFIER)) {
            return null;
        }
        IdentifierExpression identifierExpression = new IdentifierExpression(token.getValue(), token.getPosition());

        nextToken();

        if (checkToken(TokenType.DOT)) {
            nextToken();
            return parseMethodCallOrLambda(identifierExpression.getName());
        }

        if (checkToken(TokenType.BRACKET_OPEN)) {
            return parseFunctionCall(identifierExpression.getName(), identifierExpression.getPosition());
        }

        return  identifierExpression;
    }
    /*
      | identifier, [ ".", (function_call | identifier, "(" lambda_expression ")") ]
     */
    private IExpression parseMethodCallOrLambda(String firstName) throws Exception {
        if(!checkToken(TokenType.IDENTIFIER)) {
            throw new ParsingException(token.getPosition(), TokenType.IDENTIFIER, token.getType());
        }
        String name = token.getValue();
        Position position = token.getPosition();
        if (name.equals("sort")) {
            return parseLambda(firstName);
        } else {
            nextToken();
            if(token.getType() == TokenType.BRACKET_OPEN) {
                FunctionCall methodCall = parseFunctionCall(name, position);
                return new IdentiferAndMethodCallExpression(firstName, methodCall, position);
            } else {
                return new IdentiferAndFieldReference(firstName, name, position);
            }
//            List<TokenType> list = List.of(TokenType.BRACKET_OPEN, TokenType.IDENTIFIER);
//            throw new ParsingException(token.getPosition(), list, token.getType());

        }
    }

    /*
      | identifier, [ ".", (function_call | identifier, "(" lambda_expression ")") ]
     */
    private IExpression parseLambda(String name) throws Exception {
        nextToken();
        proceedAndCheck(TokenType.BRACKET_OPEN);
        proceedAndCheck(TokenType.BRACKET_OPEN);
        List<Argument> arguments = parseParameters();
        proceedAndCheck(TokenType.FLOAT.LAMBDA);
        nextToken();
        RelationExpression expression = (RelationExpression) parseExpression();
        if(!checkToken(TokenType.BRACKET_CLOSE)) {
            throw new ParsingException(token.getPosition(), TokenType.BRACKET_CLOSE, token.getType());
        }
        return new IdentifierAndLambdaCall(name, arguments, expression, arguments.get(0).getPosition());
    }

    private IExpression parseCastExpression() throws Exception {
        if(!checkToken(TokenType.CAST)) {
            return null;
        }
        Position position = token.getPosition();
        nextToken();
        List<TokenType> allowedTypes = List.of(TokenType.INTEGER, TokenType.STRING, TokenType.FLOAT, TokenType.BOOL);
        if(!checkToken(allowedTypes)) {
            throw new ParsingException(token.getPosition(), allowedTypes, token.getType());
        }
        TypeDeclaration typeDeclaration = new TypeDeclaration(Type.getTypeByName(token.getValue()), token.getPosition());

        nextToken();
        IExpression expression = parseExpression();

        return new CastExpression(typeDeclaration, expression, position);
    }

    /*
     query_statement            = "SELECT", select_clause, "FROM", identifier, [where_clause], [order_by_clause];
      Select_clause              = "(", expression, { ",", expression }, ")";
      where_clause               = "WHERE", "(", expression, ")";
     order_by_clause            = "ORDERBY", expression, ("ASC" | "DESC");  //czy tutaj identifier.identifer jest poprawnym rozwiązaniem zamiast expression?
     */
    private QueryExpression parseQueryStatement() throws Exception {
        if (!checkToken(TokenType.SELECT)) {
            return null;
        }
        Position position = token.getPosition();
        proceedAndCheck(TokenType.FLOAT.BRACKET_OPEN);
        nextToken();
        IExpression firtSelectExpression = parseExpression();
        IExpression secondSelectExpression = null;
        if (checkToken(TokenType.COMMA)) {
            nextToken();
            secondSelectExpression = parseExpression();
        }
        if (!checkToken(TokenType.BRACKET_CLOSE)) {
            throw new ParsingException(position, TokenType.BRACKET_CLOSE, token.getType());
        }
        proceedAndCheck(TokenType.FROM);
        proceedAndCheck(TokenType.IDENTIFIER);
        IdentifierExpression fromIdentifer = new IdentifierExpression(token.getValue(), token.getPosition());
        nextToken();

        IExpression whereExpression = parseWhereExpression(); //might be null

        IExpression orderByExpression = parseOrderByExpression(); //might be null
        AscOrDESC ascOrDESC = null;

        if (orderByExpression != null)
        {
             ascOrDESC = parseAscOrDesc(); //cannot be null, throws Exception
        }
        //nextToken(); //Semicolon read in calling methods
        return new QueryExpression(firtSelectExpression, secondSelectExpression, fromIdentifer, whereExpression, orderByExpression, ascOrDESC, token.getPosition());
    }

    private AscOrDESC parseAscOrDesc() {
        if(checkToken(TokenType.ASCENDING) || checkToken(TokenType.DESCENDING)) {
            AscOrDESC ascOrDESC = AscOrDESC.getBySymbol(token.getValue());
            return ascOrDESC;
        }
        return null;
    }


    private IExpression parseWhereExpression() throws Exception {
        if(!checkToken(TokenType.WHERE)) {
            return null;
        }
        proceedAndCheck(TokenType.BRACKET_OPEN);
        nextToken();
        IExpression whereExpression = parseExpression();
        if(!checkToken(TokenType.BRACKET_CLOSE)) {
            throw new ParsingException(token.getPosition(), TokenType.BRACKET_CLOSE, token.getType());
        }
        nextToken();
        return whereExpression;
    }

    private IExpression parseOrderByExpression() throws Exception {
        if(!checkToken(TokenType.ORDER_BY)) {
            return null;
        }
        proceedAndCheck(TokenType.BRACKET_OPEN);
        nextToken();
        IExpression whereExpression = parseExpression();
        if(!checkToken(TokenType.BRACKET_CLOSE)) {
            throw new ParsingException(token.getPosition(), TokenType.BRACKET_CLOSE, token.getType());
        }
        nextToken();
        return whereExpression;
    }

    private boolean checkToken(TokenType tokenType) {
        if (token.getType() != tokenType) {
            return false;
        }
        return true;
    }

    private boolean checkToken(Collection<TokenType> tokenTypes) {
        if (!tokenTypes.contains(token.getType())) {
            return false;
        }
        return true;
    }

    private void proceedAndCheck(TokenType expected) throws Exception {
        nextToken();
        if (!checkToken(expected)) {
            throw new ParsingException(token.getPosition(), expected, token.getType());
        }

    }

    private void proceedAndCheck(Collection<TokenType> expected) throws Exception {
        nextToken();
        if (!checkToken(expected)) {
            throw new ParsingException(token.getPosition(), (List<TokenType>) expected, token.getType());
        }


    }

}
