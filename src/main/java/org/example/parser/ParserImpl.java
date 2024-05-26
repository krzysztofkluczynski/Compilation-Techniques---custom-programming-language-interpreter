package org.example.parser;

import org.example.lexer.Lexer;
import org.example.lexer.error.*;
import org.example.parser.Enum.AdditiveType;
import org.example.parser.Enum.MultiplicativeType;
import org.example.parser.Enum.RelativeType;
import org.example.parser.Error.DuplicateIdentiferException;
import org.example.parser.Error.ParsingException;
import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.*;
import org.example.parser.Structure.OtherComponents.*;
import org.example.parser.Structure.Statement.*;
import org.example.parser.Enum.Type;
import org.example.token.Position;
import org.example.token.Token;
import org.example.token.TokenType;

import java.beans.Expression;
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
                List<TokenType> allowedTypes = List.of(TokenType.IF); // TODO, ADD MORE TOKEN TYPES
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

        IExpression ifExpression = parseExpression();
        proceedAndCheck(TokenType.BRACKET_CLOSE);

        nextToken();
        BlockStatement ifBlock = parseBlock();
        If ifStatement = new If(ifExpression, ifBlock, ifExpression.getPosition());
        nextToken();

        List<If> ifs = new ArrayList<>();

        while(token.getType() != TokenType.ELSE) {
            if (!checkToken(TokenType.ELIF)) {
                throw new Exception("expected elif token");
            }
            Position elifPosition = token.getPosition();
            proceedAndCheck(TokenType.BRACKET_OPEN);
            IExpression elifExpression= parseExpression();
            proceedAndCheck(TokenType.BRACKET_CLOSE);

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

        IExpression expression = parseExpression();

        proceedAndCheck(TokenType.BRACKET_CLOSE);

        nextToken();
        BlockStatement blockStatement = parseBlock();

        return new WhileStatement(expression, blockStatement, position);
    }

    /*
 function_call_or_assignment = identifier, (, "(", arguments-list, ")", | ["=", (expression | query_statement)], ),  ";";
    */
    private Node parseStatementStartingWithIdentifier() throws Exception {
        if(!checkToken(TokenType.IDENTIFIER)) {
            return null;
        }
        String name = token.getValue();
        Position identifierPosition = token.getPosition();
        nextToken();

        if(token.getType() == TokenType.BRACKET_OPEN) {
            return parseFunctionCall(name, identifierPosition);
        } else if (token.getType() == TokenType.EQUAL) {
            return parseAssignment(name, identifierPosition);
        } else {
            List<TokenType> allowedTypes = List.of(TokenType.BRACKET_OPEN, TokenType.EQUAL);
            throw new ParsingException(identifierPosition, allowedTypes, token.getType());
        }

    }

    private Statement parseAssignment(String name, Position identifierPosition) throws Exception {
        nextToken();
        IExpression expression = parseExpression();
        Statement statement;
        if (expression == null) {
            QueryStatement queryStatement = parseQueryStatement();
            if (queryStatement == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IDENTIFIER); //TODO, add more types
                throw new ParsingException(identifierPosition, allowedTypes, token.getType());
            } else {
                if(!checkToken(TokenType.SEMICOLON)) {
                    throw new ParsingException(token.getPosition(), TokenType.SEMICOLON, token.getType());
                }
                return new AssignmentWithQueryStatement(name, queryStatement, identifierPosition);
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
            nextToken();

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
     declaration_or_definition  = type, identifier, ["=", (expression | query_statement)], ";";
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
            List<TokenType> allowedTypesAfterIdentifier = List.of(TokenType.SEMICOLON, TokenType.EQUAL); //TODO, add more types
            throw new ParsingException(type.getPosition(), allowedTypesAfterIdentifier, token.getType());
        }
    }

    private Statement parseDefinition(TypeDeclaration type,String name, Position identifierPosition) throws Exception {
        nextToken();
        IExpression expression = parseExpression();
        Statement statement;
        if (expression == null) {
            QueryStatement queryStatement = parseQueryStatement();
            if (queryStatement == null) {
                List<TokenType> allowedTypes = List.of(TokenType.IDENTIFIER); //TODO, add more types
                throw new ParsingException(identifierPosition, allowedTypes, token.getType());
            } else {
                proceedAndCheck(TokenType.SEMICOLON);
                return new DefinitionWithQueryStatement(type, name, queryStatement, identifierPosition);
            }

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
                List<TokenType> allowedTypes = List.of(TokenType.IDENTIFIER, TokenType.INT_LITERAL); //TODO, add more types
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
                List<TokenType> allowedTypes = List.of(TokenType.IDENTIFIER, TokenType.INT_LITERAL); //TODO, change types
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
                List<TokenType> allowedTypes = List.of(TokenType.IDENTIFIER, TokenType.INT_LITERAL); //TODO, change types
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
                List<TokenType> allowedTypes = List.of(TokenType.IDENTIFIER, TokenType.INT_LITERAL); //TODO, change types
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
                List<TokenType> allowedTypes = List.of(TokenType.IDENTIFIER, TokenType.INT_LITERAL); //TODO, change types
                throw new ParsingException(token.getPosition(), allowedTypes, token.getType());
            }
            left = new MultiplicativeExpression(left,multiplicativeType, right, left.getPosition());
        }
        return left;
    }

    private IExpression parseNegatedFactor() throws Exception {
        boolean negated = false;
        Position position = token.getPosition();
        List<TokenType> allowedTypes = List.of(TokenType.NOT, TokenType.MINUS); //TODO, change types
        if (checkToken(allowedTypes)) {
            negated = true;
        }
        IExpression expression = parseFactor();
        if (negated && expression == null) {
            List<TokenType> allowedForException = List.of(TokenType.IDENTIFIER, TokenType.INT_LITERAL); //TODO, change types
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
            List<IExpression> literals = parseListLiteral();
            expression = new LiteralList(literals, position);
        }
        if (checkToken(TokenType.BRACKET_OPEN)) {
            expression = parseTupleLiteral();
        }
        if (checkToken(TokenType.PIPE)) {
            Map<IExpression, IExpression> map = parseDictionaryLiteral();
            expression = new LiteralDictionary(map, position);
        }
        nextToken();
        return expression;
    }

    private Map<IExpression,IExpression> parseDictionaryLiteral() throws Exception {
        if(!checkToken(TokenType.BRACKET_OPEN)) {
            return null;
        }
        nextToken();
        IExpression objectOne;
        IExpression objectTwo;
        Map<IExpression, IExpression> map = null;
        
        while(!checkToken(TokenType.PIPE)) {


            objectOne = parseLiteral();
            if(!checkToken(TokenType.COLON)) {
                throw new ParsingException(token.getPosition(), TokenType.COLON, token.getType());
            }
            nextToken();
            objectTwo = parseLiteral();
            
            map.put(objectOne, objectTwo);

            List<TokenType> list = List.of(TokenType.COMMA, TokenType.PIPE);
            if(!checkToken(list)) {
                throw new ParsingException(token.getPosition(), list, token.getType());
            }
            nextToken();
            
        }
        return map;
    }

    private IExpression parseTupleLiteral() throws Exception {
        if(!checkToken(TokenType.BRACKET_OPEN)) {
            return null;
        }
        nextToken();
        IExpression objectOne;
        IExpression objectTwo;

        objectOne = parseLiteral();
        if(!checkToken(TokenType.COMMA)) {
            throw new ParsingException(token.getPosition(), TokenType.COMMA, token.getType());
        }
            nextToken();
            objectTwo = parseLiteral();
            if(!checkToken(TokenType.BRACKET_CLOSE)) {
                throw new ParsingException(token.getPosition(), TokenType.BRACKET_CLOSE, token.getType());
            }
            return new LiteralTuple(objectOne, objectTwo, token.getPosition());
    }

    private List<IExpression> parseListLiteral() throws Exception {
        if(!checkToken(TokenType.SQUARE_BRACKET_OPEN)) {
            return null;
        }
        nextToken();
        List<IExpression> literals = null;

        while(!checkToken(TokenType.SQUARE_BRACKET_CLOSE)) {
            IExpression literal = parseLiteral();

            literals.add(literal);

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
    private IExpression parseStartingwithIdentifier() throws ReachedEOFException, StringMaxSizeExceeded, UnkownTokenException, IntMaxValueExceededException, IOException, IdentifierTooLongException, DecimalMaxValueExceededException {
        if (!checkToken(TokenType.IDENTIFIER)) {
            return null;
        }
        IdentifierExpression identifierExpression = new IdentifierExpression(token.getValue(), token.getPosition());

        nextToken();
        if(checkToken(TokenType.SEMICOLON)) {
            return identifierExpression;
        }
        return null;
    }

    private IExpression parseCastExpression() {
        return null; //TODO
    }

    private QueryStatement parseQueryStatement() {
        return null; //TODO
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
