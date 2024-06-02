package org.example.interpreter;

import org.example.interpreter.error.IncorrectDefaultPrintFunctionCallInterpretingException;
import org.example.interpreter.error.InterpretingException;
import org.example.interpreter.error.NoMainFunctionInterpretingException;
import org.example.interpreter.error.NoSuchFunctionInterpretingException;
import org.example.interpreter.model.FunctionCallContext;
import org.example.interpreter.model.Print;
import org.example.interpreter.model.VisitationResult;
import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.*;
import org.example.parser.Structure.OtherComponents.Argument;
import org.example.parser.Structure.OtherComponents.FunctionDefinition;
import org.example.parser.Structure.OtherComponents.If;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.parser.Structure.Statement.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InterpretingVisitor  implements Visitor {

    private final List<FunctionCallContext> functionCallContexts = new ArrayList<>();

    private VisitationResult lastVisitationResult = null;
    private final Program program;

    public InterpretingVisitor(Program program) {
        this.program = program;
    }

    public Object getInterpretingResult() throws InterpretingException {
        program.accept(this);
        return lastVisitationResult;
    }


    @Override
    public void visit(Program program) throws InterpretingException {
        Map<String, FunctionDefinition> functionDefinitions = program.getFunctionDefinitions();
        if (!functionDefinitions.keySet().contains("main")) {
            throw new NoMainFunctionInterpretingException();
        }

        FunctionDefinition mainDefinition = functionDefinitions.get("main");
        FunctionCall startingFunctionCall = new FunctionCall(mainDefinition.getName(), null, mainDefinition.getPosition());
        startingFunctionCall.accept(this);

    }

    @Override
    public void visit(FunctionDefinition functionDefinition) {

    }

    @Override
    public void visit(Argument argument) {

    }

    @Override
    public void visit(BlockStatement blockStatement) {

    }

    @Override
    public void visit(MultiplicativeExpression multiplicativeExpression) {

    }

    @Override
    public void visit(ArthmeticExpression arthmeticExpression) {

    }

    @Override
    public void visit(NegatedExpression negatedExpression) {

    }

    @Override
    public void visit(CastExpression castExpression) {

    }

    @Override
    public void visit(LiteralList literalList) {

    }

    @Override
    public void visit(LiteralTuple literalTuple) {

    }

    @Override
    public void visit(IdentiferAndMethodCallExpression identiferAndMethodCallExpression) {

    }

    @Override
    public void visit(LambdaExpression lambdaExpression) {

    }

    @Override
    public void visit(LiteralBool literalBool) {

    }

    @Override
    public void visit(LiteralDictionary literalDictionary) {

    }

    @Override
    public void visit(LiteralFloat literalFloat) {

    }

    @Override
    public void visit(LiteralInteger literalInteger) {

    }

    @Override
    public void visit(LiteralString literalString) {

    }

    @Override
    public void visit(AndExpression andExpression) {

    }

    @Override
    public void visit(IdentifierAndLambdaCall identifierAndLambdaCall) {

    }

    @Override
    public void visit(IdentifierExpression identifierExpression) {

    }

    @Override
    public void visit(OrExpression orExpression) {

    }

    @Override
    public void visit(RelationExpression relationExpression) {

    }

    @Override
    public void visit(If anIf) {

    }

    @Override
    public void visit(AssignmentWithExpressionStatement assignmentWithExpressionStatement) {

    }

    @Override
    public void visit(AssignmentWithQueryStatement assignmentWithQueryStatement) {

    }

    @Override
    public void visit(ConditionalStatement conditionalStatement) {

    }

    @Override
    public void visit(DeclarationStatement declarationStatement) {

    }

    @Override
    public void visit(DefinitionWithExpressionStatement definitionWithExpressionStatement) {

    }

    @Override
    public void visit(DefinitionWithQueryStatement definitionWithQueryStatement) {

    }

    @Override
    public void visit(ForStatement forStatement) {

    }

    public void visit(FunctionCall functionCall) throws InterpretingException {
        FunctionDefinition functionDefinition = program.getFunctionDefinitions().get(functionCall.getName());
        if (functionDefinition == null) {
            if ("print".equals(functionCall.getName())) {
                visitDefaultPrintFunctionCall(functionCall);
                return;
            }
            throw new NoSuchFunctionInterpretingException(functionCall);
        }

    }

    @Override
    public void visit(QueryStatement queryStatement) {

    }

    @Override
    public void visit(ReturnStatement returnStatement) {

    }

    @Override
    public void visit(WhileStatement whileStatement) {

    }

    private void visitDefaultPrintFunctionCall(FunctionCall functionCall) throws InterpretingException {
        List<Object> parsedArguments = getFunctionCallArgumentsAsValues(functionCall);
        if (parsedArguments.size() == 1) {
            Print printFunction = new Print((String) parsedArguments.get(0));
            printFunction.execute();
            lastVisitationResult.setReturnedValue(null);
        } else {
            throw new IncorrectDefaultPrintFunctionCallInterpretingException(parsedArguments);
        }
    }

    private List<Object> getFunctionCallArgumentsAsValues(FunctionCall functionCall) throws InterpretingException {
        List<Object> parsedArguments = new ArrayList<>();
        for (IExpression arg: functionCall.getArguments()) {
            arg.accept(this);
            parsedArguments.add(lastVisitationResult.getReturnedValue().getValue());
        }
        return parsedArguments;
    }


}
