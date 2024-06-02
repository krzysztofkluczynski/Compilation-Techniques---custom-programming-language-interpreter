package org.example.parser;

import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.*;
import org.example.parser.Structure.OtherComponents.*;
import org.example.parser.Structure.Statement.BlockStatement;
import org.example.parser.Structure.Statement.*;

public interface Visitor {
    void visit(Program program);
    void visit(FunctionDefinition functionDefinition);

    void visit(Argument argument);

    void visit(BlockStatement blockStatement);

    void accept(ConditionalStatement conditionalStatement);

    void accept(DeclarationStatement declarationStatement);

    void accept(ForStatement forStatement);

    void accept(FunctionCall functionCall);

    void accept(ReturnStatement returnStatement);

    void accept(WhileStatement whileStatement);

    void accept(If elif);

    void accept(IExpression expression);

    void accept(QueryExpression queryExpression);
    void accept(AssignmentWithExpressionStatement assignmentWithExpressionStatement);


    void accept(AssignmentWithQueryStatement assignmentWithQueryStatement);

    void accept(DefinitionWithExpressionStatement definitionWithExpressionStatement);

    void accept(DefinitionWithQueryStatement definitionWithQueryStatement);

    void visit(MultiplicativeExpression multiplicativeExpression);

    void visit(ArthmeticExpression arthmeticExpression);

    void visit(NegatedExpression negatedExpression);

    void visit(CastExpression castExpression);

    void accept(LiteralInteger literalInteger);

    void visit(LiteralList literalList);

    void visit(LiteralTuple literalTuple);

    void visit(IdentiferAndMethodCallExpression identiferAndMethodCallExpression);

    void visit(IdentifierAndLambdaCall identifierAndLambdaCall);

    void visit(IdentiferAndFieldReference identiferAndFieldReference);

    void visit(LiteralBool literalBool);
}
