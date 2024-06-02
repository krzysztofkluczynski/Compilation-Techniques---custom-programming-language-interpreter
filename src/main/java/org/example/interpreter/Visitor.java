package org.example.interpreter;

import org.example.interpreter.error.InterpretingException;
import org.example.interpreter.error.NoMainFunctionInterpretingException;
import org.example.interpreter.error.NoSuchFunctionInterpretingException;
import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.*;
import org.example.parser.Structure.OtherComponents.*;
import org.example.parser.Structure.Statement.BlockStatement;
import org.example.parser.Structure.Statement.*;

public interface Visitor {
    void visit(Program program) throws InterpretingException;
    void visit(FunctionDefinition functionDefinition);

    void visit(Argument argument);

    void visit(BlockStatement blockStatement);


    void visit(MultiplicativeExpression multiplicativeExpression);

    void visit(ArthmeticExpression arthmeticExpression);

    void visit(NegatedExpression negatedExpression);

    void visit(CastExpression castExpression);


    void visit(LiteralList literalList);

    void visit(LiteralTuple literalTuple);

    void visit(IdentiferAndMethodCallExpression identiferAndMethodCallExpression);

    void visit(LambdaExpression lambdaExpression);


    void visit(LiteralBool literalBool);

    void visit(LiteralDictionary literalDictionary);

    void visit(LiteralFloat literalFloat);

    void visit(LiteralInteger literalInteger);

    void visit(LiteralString literalString);

    void visit(AndExpression andExpression);

    void visit(IdentifierAndLambdaCall identifierAndLambdaCall);

    void visit(IdentifierExpression identifierExpression);

    void visit(OrExpression orExpression);

    void visit(RelationExpression relationExpression);

    void visit(If anIf);

    void visit(AssignmentWithExpressionStatement assignmentWithExpressionStatement);

    void visit(AssignmentWithQueryStatement assignmentWithQueryStatement);

    void visit(ConditionalStatement conditionalStatement);

    void visit(DeclarationStatement declarationStatement);

    void visit(DefinitionWithExpressionStatement definitionWithExpressionStatement);

    void visit(DefinitionWithQueryStatement definitionWithQueryStatement);

    void visit(ForStatement forStatement);

    void visit(FunctionCall functionCall) throws InterpretingException;

    void visit(QueryStatement queryStatement);

    void visit(ReturnStatement returnStatement);

    void visit(WhileStatement whileStatement);
}
