package org.example.interpreter;

import org.example.interpreter.error.*;
import org.example.interpreter.model.*;
import org.example.parser.Enum.Type;
import org.example.parser.Node;
import org.example.parser.Structure.Expression.*;
import org.example.parser.Structure.Expression.Literals.*;
import org.example.parser.Structure.OtherComponents.Argument;
import org.example.parser.Structure.OtherComponents.FunctionDefinition;
import org.example.parser.Structure.OtherComponents.If;
import org.example.parser.Structure.OtherComponents.Program;
import org.example.parser.Structure.Statement.*;

import java.util.ArrayList;
import java.util.HashMap;
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
        return lastVisitationResult.getReturnedValue().getValue();
    }


    @Override
    public void visit(Program program) throws InterpretingException {
        Map<String, FunctionDefinition> functionDefinitions = program.getFunctionDefinitions();
        if (!functionDefinitions.keySet().contains("main")) {
            throw new NoMainFunctionInterpretingException();
        }

        FunctionDefinition mainDefinition = functionDefinitions.get("main");
        FunctionCall startingFunctionCall = new FunctionCall(mainDefinition.getName(), new ArrayList<>(), mainDefinition.getPosition());
        startingFunctionCall.accept(this);

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
        List<Object> parsedArguments = getFunctionCallArgumentsAsValues(functionCall);

        Map<String, Variable> localVariables = parseParamsAndArgumentsToVariables(functionDefinition.getArguments(), parsedArguments);

        FunctionCallContext functionCallContext = new FunctionCallContext(functionDefinition.getReturnType().getType(), functionCall.getName(), localVariables);
        functionCallContexts.add(functionCallContext);

        functionDefinition.accept(this);

        assertCorrectReturnedValueType(functionCallContext, lastVisitationResult);

        lastVisitationResult.setValueReturned(false);

        removeLastFunctionCallContext(functionCallContexts);
    }

    private void visitDefaultPrintFunctionCall(FunctionCall functionCall) throws InterpretingException {
        List<Object> parsedArguments = getFunctionCallArgumentsAsValues(functionCall);
        if (parsedArguments.size() == 1) {
            Print printFunction = new Print((String) parsedArguments.get(0));
            printFunction.execute();
            lastVisitationResult.setReturnedValue(new Variable(Type.INT,"some", 1));
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


    @Override
    public void visit(FunctionDefinition functionDefinition) throws InterpretingException {
        functionDefinition.getBody().accept(this);
    }

    @Override
    public void visit(BlockStatement blockStatement) throws InterpretingException {
        FunctionCallContext functionCallContext = getLastFunctionCallContext(functionCallContexts);
        functionCallContext.addScope(new Scope());
        List<Statement> instructions = blockStatement.getInstructions();
        for (Statement instruction: instructions) {
            instruction.accept(this);
            if (lastVisitationResult.wasValueReturned()) {
                break;
            }
        }
        functionCallContext.removeLastScope();

    }


    @Override
    public void visit(AssignmentWithExpressionStatement assignmentWithExpressionStatement) throws InterpretingException {
        FunctionCallContext functionCallContext = getLastFunctionCallContext(functionCallContexts);
        assignmentWithExpressionStatement.getExpression().accept(this);

        functionCallContext.updateVariable(assignmentWithExpressionStatement.getIdentifierName(), lastVisitationResult.getReturnedValue().getValue());
    }

    @Override
    public void visit(AssignmentWithQueryStatement assignmentWithQueryStatement) throws NoSuchVariableInterpretingException {
        FunctionCallContext functionCallContext = getLastFunctionCallContext(functionCallContexts);
        assignmentWithQueryStatement.getQueryExpression().accept(this);

        functionCallContext.updateVariable(assignmentWithQueryStatement.getIdentifierName(), lastVisitationResult.getReturnedValue().getValue());
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


    @Override
    public void visit(ReturnStatement returnStatement) throws InterpretingException {
        returnStatement.getExpression().accept(this);
        lastVisitationResult.setValueReturned(true);

    }



    @Override
    public void visit(WhileStatement whileStatement) {

    }


    @Override
    public void visit(Argument argument) {

    }

    @Override
    public void visit(OrExpression orExpression) {

    }

    @Override
    public void visit(AndExpression andExpression) {

    }

    @Override
    public void visit(RelationExpression relationExpression) {

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
    public void visit(LiteralDictionary literalDictionary) {

    }

    @Override
    public void visit(LiteralBool literalBool) {
        lastVisitationResult = new VisitationResult(new Variable(Type.BOOL, literalBool.getValue()));
    }

    @Override
    public void visit(LiteralFloat literalFloat) {
        lastVisitationResult = new VisitationResult(new Variable(Type.FLOAT, literalFloat.getValue()));
    }

    @Override
    public void visit(LiteralInteger literalInteger) {
        lastVisitationResult = new VisitationResult(new Variable(Type.INT, literalInteger.getValue()));
    }

    @Override
    public void visit(LiteralString literalString) {
        lastVisitationResult = new VisitationResult(new Variable(literalString.getValue()));
    }


    @Override
    public void visit(IdentifierAndLambdaCall identifierAndLambdaCall) {

    }

    @Override
    public void visit(IdentifierExpression identifierExpression) {

    }



    @Override
    public void visit(If anIf) {

    }



    @Override
    public void visit(QueryExpression queryExpression) {

    }

    @Override
    public void visit(IdentiferAndFieldReference identiferAndFieldReference) {

    }

    public static void assertCorrectReturnedValueType(FunctionCallContext functionCallContext, VisitationResult visitationResult) throws InterpretingException {
        Type functionType = functionCallContext.getFunctionType();
        if (visitationResult == null || !visitationResult.wasValueReturned() || visitationResult.getReturnedValue() == null) {
            throw new NoValueReturnedFromFunctionInterpretingException(functionType.getName());
        }
        Type returnedType = visitationResult.getReturnedValue().getVariableType();
        if (returnedType != functionType) {
            throw new IncorrectReturnedValueTypeInterpretingException(functionCallContext, returnedType);
        }
    }

    public static void removeLastFunctionCallContext(List<FunctionCallContext> functionCallContextList) {
        functionCallContextList.remove(functionCallContextList.size() - 1);
    }
//
    public static FunctionCallContext getLastFunctionCallContext(List<FunctionCallContext> functionCallContextList) {
        return functionCallContextList.get(functionCallContextList.size() - 1);
    }
//
    public static Map<String, Variable> parseParamsAndArgumentsToVariables(List<Argument> params, List<Object> arguments) throws InterpretingException {
        if (params.size() != arguments.size()) {
            throw new InvalidFunctionCallInterpretingException(params, arguments);
        }
        Map<String, Variable> variables = new HashMap<>();
        for (int i = 0; i < params.size(); i++) {
            Argument currParam = params.get(i);
            Object currArg = arguments.get(i);
            Variable nextVar = new Variable(currParam.getType().getType(), currParam.getName(), currArg);
            variables.put(nextVar.getName(), nextVar);
        }
        return variables;
    }






}
