package org.example.interpreter;

import org.example.interpreter.error.*;
import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.*;
import org.example.parser.Structure.OtherComponents.*;
import org.example.parser.Structure.Statement.BlockStatement;
import org.example.parser.Structure.Statement.*;

public interface Visitor {
    void visit(Program program) throws InterpretingException;
    void visit(FunctionDefinition functionDefinition) throws InterpretingException;

    void visit(BlockStatement blockStatement) throws InterpretingException;


    void visit(MultiplicativeExpression multiplicativeExpression) throws InterpretingException;

    void visit(ArthmeticExpression arthmeticExpression) throws InterpretingException;

    void visit(NegatedExpression negatedExpression) throws InterpretingException;

    void visit(CastExpression castExpression) throws InterpretingException;


    void visit(LiteralList literalList);

    void visit(LiteralTuple literalTuple);

    void visit(IdentiferAndMethodCallExpression identiferAndMethodCallExpression) throws InterpretingException;

    void visit(LiteralBool literalBool);

    void visit(LiteralDictionary literalDictionary);

    void visit(LiteralFloat literalFloat);

    void visit(LiteralInteger literalInteger);

    void visit(LiteralString literalString);

    void visit(AndExpression andExpression) throws InterpretingException;

    void visit(IdentifierAndLambdaCall identifierAndLambdaCall);

    void visit(IdentifierExpression identifierExpression) throws NoSuchVariableInterpretingException;

    void visit(OrExpression orExpression) throws InterpretingException;

    void visit(RelationExpression relationExpression) throws InterpretingException;

//    void visit(If anIf);

    void visit(AssignmentWithExpressionStatement assignmentWithExpressionStatement) throws InterpretingException;

    void visit(AssignmentWithQueryStatement assignmentWithQueryStatement) throws NoSuchVariableInterpretingException;

    void visit(ConditionalStatement conditionalStatement) throws InterpretingException;

    void visit(DeclarationStatement declarationStatement) throws InterpretingException;

    void visit(DefinitionWithExpressionStatement definitionWithExpressionStatement) throws InterpretingException;

    void visit(DefinitionWithQueryStatement definitionWithQueryStatement);

    void visit(ForStatement forStatement) throws InterpretingException;

    void visit(FunctionCall functionCall) throws InterpretingException;

    void visit(ReturnStatement returnStatement) throws InterpretingException;

    void visit(WhileStatement whileStatement) throws InterpretingException;

    void visit(QueryExpression queryExpression);

    void visit(IdentiferAndFieldReference identiferAndFieldReference);
}
