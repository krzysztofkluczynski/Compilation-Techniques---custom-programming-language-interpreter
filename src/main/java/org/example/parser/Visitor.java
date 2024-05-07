package org.example.parser;

import org.example.parser.Structure.OtherComponents.Argument;
import org.example.parser.Structure.OtherComponents.Elif;
import org.example.parser.Structure.Expression.Expression;
import org.example.parser.Structure.Statement.BlockStatement;
import org.example.parser.Structure.OtherComponents.FunctionDefinition;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.parser.Structure.Statement.*;

public interface Visitor {
    void visit(Program program);
    void visit(FunctionDefinition functionDefinition);

    void visit(Argument argument);

    void visit(BlockStatement blockStatement);

    void accept(AssignmentStatement assignmentStatement);

    void accept(ConditionalStatement conditionalStatement);

    void accept(DeclarationStatement declarationStatement);

    void accept(ForStatement forStatement);

    void accept(FunctionCall functionCall);

    void accept(ReturnStatement returnStatement);

    void accept(WhileStatement whileStatement);

    void accept(Elif elif);

    void accept(Expression expression);
}
