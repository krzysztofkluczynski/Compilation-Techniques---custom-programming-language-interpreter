package org.example.interpreter;

import org.example.interpreter.error.*;
import org.example.interpreter.model.*;
import org.example.parser.Enum.*;
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
    public void visit(ConditionalStatement conditionalStatement) throws InterpretingException {
        List<If> ifs = conditionalStatement.getIfs();
        boolean ifExecuteElse = true;

        for (int i = 0; i < ifs.size(); i++) {
            If anIf = ifs.get(i);
            // Check if this is the last 'If' in the list
            boolean isLastIf = (i == ifs.size() - 1);

            if (anIf.getExpression() != null) {
                anIf.getExpression().accept(this);
                Object condition = lastVisitationResult.getReturnedValue().getValue();
                if (Boolean.TRUE.equals(condition)) {
                    ifExecuteElse = false;
                    anIf.getBlockStatement().accept(this);
                }
            } else if (isLastIf && ifExecuteElse) {
                anIf.getBlockStatement().accept(this);
            }
        }

    }

    @Override
    public void visit(ForStatement forStatement) throws InterpretingException {
        Type mainType = forStatement.getType().getType();
        Type firstOptionalType = forStatement.getType().getFirstOptionalParam();
        Type secondOptionalType = forStatement.getType().getSecondOptionalParam();

        Variable variable = getLastFunctionCallContext(functionCallContexts).getVariable(forStatement.getCollectionIdentifer());
        BlockStatement block = forStatement.getBlockStatement();  // Assuming you have a method to get the BlockStatement


        if (variable.getVariableType().equals(Type.DICTIONARY)) {
            if (variable.getOptionalOne() != firstOptionalType || variable.getOptionalTwo() != secondOptionalType) {
                throw new UnableToAssignVariableException(variable, mainType);
            }
            Map<Object, Object> map = (Map<Object, Object>) variable.getValue();
            for (Map.Entry<Object, Object> entry : map.entrySet()) {

                Pair newPair = new Pair(entry.getKey(), entry.getValue());
                FunctionCallContext functionCallContext = getLastFunctionCallContext(functionCallContexts);
                Variable newVariable = new Variable(Type.TUPLE, variable.getOptionalOne(), variable.getOptionalTwo(), forStatement.getIdentifer(), newPair);
                functionCallContext.addLocalVariableToLastScope(newVariable);

                block.accept(this);

                functionCallContext.removeLocalVariable(newVariable);
            }
        } else if (variable.getVariableType().equals(Type.LIST)) {
            if (variable.getOptionalOne() != mainType) {
                throw new UnableToAssignVariableException(variable, mainType);
            }
            List<Object> list = (List<Object>) variable.getValue();
            for (Object item : list) {

                FunctionCallContext functionCallContext = getLastFunctionCallContext(functionCallContexts);
                Variable newVariable = new Variable(variable.getOptionalOne(), forStatement.getIdentifer(), item);
                functionCallContext.addLocalVariableToLastScope(newVariable);

                block.accept(this);

                functionCallContext.removeLocalVariable(newVariable);
            }
        } else {
            throw new InterpretingException("Invalid type for for loop: " + variable.getVariableType());
        }
    }


    @Override
    public void visit(ReturnStatement returnStatement) throws InterpretingException {
        returnStatement.getExpression().accept(this);
        lastVisitationResult.setValueReturned(true);

    }


    @Override
    public void visit(WhileStatement whileStatement) throws InterpretingException {
        whileStatement.getExpression().accept(this);
        Object condition = lastVisitationResult.getReturnedValue().getValue();
        while (Boolean.TRUE.equals(condition)) {
            whileStatement.getBlockStatement().accept(this);
            if (lastVisitationResult.wasValueReturned()) {
                return;
            } else {
                whileStatement.getExpression().accept(this);
                condition = lastVisitationResult.getReturnedValue().getValue();
            }
        }


    }

    //gdy zmienna już istnieje, a = 5;
    @Override
    public void visit(AssignmentWithExpressionStatement assignmentWithExpressionStatement) throws InterpretingException {
        FunctionCallContext functionCallContext = getLastFunctionCallContext(functionCallContexts);
        assignmentWithExpressionStatement.getExpression().accept(this);
        functionCallContext.updateVariable(assignmentWithExpressionStatement.getIdentifierName(), lastVisitationResult.getReturnedValue().getValue());
    }

    //gdy zmienna nie istnieje,sama deklaracja, int a;;
    @Override
    public void visit(DeclarationStatement declarationStatement) throws InterpretingException {


        if (declarationStatement.getType().getType() == Type.TUPLE ) {
            Type firstOptionalType = declarationStatement.getType().getFirstOptionalParam();
            Type secondOptionalType = declarationStatement.getType().getSecondOptionalParam();
            lastVisitationResult = new VisitationResult(
                    new Variable(Type.TUPLE, firstOptionalType, secondOptionalType, declarationStatement.getName()));

        } else if (declarationStatement.getType().getType() == Type.DICTIONARY) {
            Type firstOptionalType = declarationStatement.getType().getFirstOptionalParam();
            Type secondOptionalType = declarationStatement.getType().getSecondOptionalParam();
            lastVisitationResult = new VisitationResult(
                    new Variable(Type.DICTIONARY, firstOptionalType, secondOptionalType, declarationStatement.getName()));

        } else if (declarationStatement.getType().getType() == Type.LIST) {
            Type firstOptionalType = declarationStatement.getType().getFirstOptionalParam();
            lastVisitationResult = new VisitationResult(
                    new Variable(Type.LIST, firstOptionalType, declarationStatement.getName()));

        } else {
            lastVisitationResult = new VisitationResult(
                    new Variable(declarationStatement.getType().getType(), declarationStatement.getName()));
        }


        getLastFunctionCallContext(functionCallContexts).addLocalVariableToLastScope(lastVisitationResult.getReturnedValue());
    }

    //deklaracja z przypisaniem, int a = 5;
    @Override
    public void visit(DefinitionWithExpressionStatement definitionWithExpressionStatement) throws InterpretingException {
        IExpression assignedValue = definitionWithExpressionStatement.getExpression();
        assignedValue.accept(this);

        if (definitionWithExpressionStatement.getType().getType() == Type.TUPLE ) {
            Type firstOptionalType = definitionWithExpressionStatement.getType().getFirstOptionalParam();
            Type secondOptionalType = definitionWithExpressionStatement.getType().getSecondOptionalParam();
            lastVisitationResult = new VisitationResult(
                    new Variable(Type.TUPLE, firstOptionalType, secondOptionalType, definitionWithExpressionStatement.getIdentifierName(), lastVisitationResult.getReturnedValue().getValue()));

        } else if (definitionWithExpressionStatement.getType().getType() == Type.DICTIONARY) {
            Type firstOptionalType = definitionWithExpressionStatement.getType().getFirstOptionalParam();
            Type secondOptionalType = definitionWithExpressionStatement.getType().getSecondOptionalParam();
            lastVisitationResult = new VisitationResult(
                    new Variable(Type.DICTIONARY, firstOptionalType, secondOptionalType, definitionWithExpressionStatement.getIdentifierName(), lastVisitationResult.getReturnedValue().getValue()));

        } else if (definitionWithExpressionStatement.getType().getType() == Type.LIST) {
            Type firstOptionalType = definitionWithExpressionStatement.getType().getFirstOptionalParam();
            lastVisitationResult = new VisitationResult(
                    new Variable(Type.LIST, firstOptionalType, definitionWithExpressionStatement.getIdentifierName(), lastVisitationResult.getReturnedValue().getValue()));

        } else {
            lastVisitationResult = new VisitationResult(
                    new Variable(definitionWithExpressionStatement.getType().getType(), definitionWithExpressionStatement.getIdentifierName(), lastVisitationResult.getReturnedValue().getValue()));
        }


        getLastFunctionCallContext(functionCallContexts).addLocalVariableToLastScope(lastVisitationResult.getReturnedValue());
    }

    @Override
    public void visit(IdentiferAndMethodCallExpression identiferAndMethodCallExpression) throws InterpretingException {
        String variableName = identiferAndMethodCallExpression.getName();
        FunctionCall methodCall = identiferAndMethodCallExpression.getMethodCall();
        List<Object> parsedArguments = getFunctionCallArgumentsAsValues(methodCall);

        Variable collectionVariable = getLastFunctionCallContext(functionCallContexts).getVariable(variableName);

        Type collectionType = collectionVariable.getVariableType();
        Type firstOptionalType = collectionVariable.getOptionalOne();
        Type secondOptionalType = collectionVariable.getOptionalTwo();

        if (collectionType == Type.LIST) {
            List<Object> list = (List<Object>) collectionVariable.getValue();

            switch(methodCall.getName()) {
                case "add":
                    if (parsedArguments.size() != 1) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 1, parsedArguments.size());
                    }
                    list.add(parsedArguments.get(0));
                    break;
                case "delete":
                    if (parsedArguments.size() != 1) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 1, parsedArguments.size());
                    }
                    list.remove(parsedArguments.get(0));
                    break;
                case "get":
                    if (parsedArguments.size() != 1) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 1, parsedArguments.size());
                    }
                    lastVisitationResult = new VisitationResult(new Variable(firstOptionalType, list.get((int)parsedArguments.get(0))));
                    break;
                case "set":
                    if (parsedArguments.size() != 2) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 1, parsedArguments.size());
                    }
                    list.set((Integer) parsedArguments.get(0), parsedArguments.get(1));
                    break;
            }

            getLastFunctionCallContext(functionCallContexts).updateVariable(variableName, list);

        } else if (collectionType == Type.DICTIONARY) {
            Map<Object, Object> map = (Map<Object, Object>) collectionVariable.getValue();
            switch (methodCall.getName()) {
                case "add":
                    if (parsedArguments.size() != 2) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 2, parsedArguments.size());
                    }
                    map.put(parsedArguments.get(0), parsedArguments.get(1));
                    break;
                case "delete":
                    if (parsedArguments.size() != 1) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 1, parsedArguments.size());
                    }
                    map.remove(parsedArguments.get(0));
                    break;
                case "get":
                    if (parsedArguments.size() != 1) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 1, parsedArguments.size());
                    }
                    lastVisitationResult = new VisitationResult(new Variable(secondOptionalType, map.get(parsedArguments.get(0))));
                    break;
                case "set":
                    if (parsedArguments.size() != 2) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 2, parsedArguments.size());
                    }
                    if (!map.containsKey(parsedArguments.get(0))) {
                        throw new InterpretingException("Key does not exist in the dictionary");
                    }
                    map.put(parsedArguments.get(0), parsedArguments.get(1));
                    break;
                case "ifexists":
                    if (parsedArguments.size() != 1) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 1, parsedArguments.size());
                    }
                    lastVisitationResult = new VisitationResult(new Variable(Type.BOOL, map.containsKey(parsedArguments.get(0))));
                    break;
            }

            getLastFunctionCallContext(functionCallContexts).updateVariable(variableName, map);


        } else if (collectionType == Type.TUPLE) {
            Pair pair = (Pair) collectionVariable.getValue();
            switch (methodCall.getName()) {
                case "get":
                    if (parsedArguments.size() != 1) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 1, parsedArguments.size());
                    }
                    int index = (int) parsedArguments.get(0);
                    Object result = index == 0 ? pair.getFirst() : index == 1 ? pair.getSecond() : null;
                    lastVisitationResult = new VisitationResult(new Variable(index == 0 ? firstOptionalType : index == 1 ? secondOptionalType : null, result));
                    break;
                case "set":
                    if (parsedArguments.size() != 2) {
                        throw new InvalidNumberofArgumentsException(methodCall.getName(), 2, parsedArguments.size());
                    }
                    int indexSet = (int) parsedArguments.get(0);
                    if (indexSet == 0) {
                        pair.setFirst(parsedArguments.get(1));
                    } else if (indexSet == 1) {
                        pair.setSecond(parsedArguments.get(1));
                    } else {
                        throw new InterpretingException("Invalid index for tuple: " + indexSet);
                    }

                    break;
            }
            getLastFunctionCallContext(functionCallContexts).updateVariable(variableName, pair);
        } else {
            throw new InterpretingException("Invalid type for method call: " + collectionType);
        }
    }





    @Override
    public void visit(DefinitionWithQueryStatement definitionWithQueryStatement) {

    }

    @Override
    public void visit(AssignmentWithQueryStatement assignmentWithQueryStatement) throws InterpretingException {
        FunctionCallContext functionCallContext = getLastFunctionCallContext(functionCallContexts);
        assignmentWithQueryStatement.getQueryExpression().accept(this);
        functionCallContext.updateVariable(assignmentWithQueryStatement.getIdentifierName(), lastVisitationResult.getReturnedValue().getValue());

    }



    @Override
    public void visit(IdentiferAndFieldReference identiferAndFieldReference) {

    }

    @Override
    public void visit(QueryExpression queryExpression) throws InterpretingException {

        IdentiferAndFieldReference identiferAndFieldReferenceFirst = (IdentiferAndFieldReference) queryExpression.getFirstSelectExpression();
        IdentiferAndFieldReference identiferAndFieldReferenceSecond = (IdentiferAndFieldReference) queryExpression.getSecondSelectExpression();

        IdentifierExpression identifierExpression = queryExpression.getFromIdentifer();

        Variable dictionaryVariable = getLastFunctionCallContext(functionCallContexts).getVariable(identifierExpression.getName());

        if (dictionaryVariable.getVariableType() != Type.DICTIONARY) {
            throw new InterpretingException("Invalid type for query: " + dictionaryVariable.getVariableType());
        }

        RelationExpression whereExpression = (RelationExpression) queryExpression.getWhereExpression();



        IdentiferAndFieldReference orderByExpression = (IdentiferAndFieldReference) queryExpression.getOrderByExpression();




        Map<Object, Object> dictionary = (Map<Object, Object>) dictionaryVariable.getValue();

        List<Object> resultList = new ArrayList<>();
        Map<Object, Object> resultMap = new HashMap<>();
        Type typeKey = dictionaryVariable.getOptionalOne();
        Type typeValue = dictionaryVariable.getOptionalTwo();
        Type listType = null;


        if (identiferAndFieldReferenceFirst == null && identiferAndFieldReferenceSecond == null) {
            throw new InterpretingException("Invalid field reference in query");
        }

        //order by part
        if (orderByExpression != null) {
            String orderByString = orderByExpression.getSecondIdentiferName();
            AscOrDESC ascOrDESC = queryExpression.getAscOrDESC();
            if (orderByString.equals("key")) {
                if (ascOrDESC == AscOrDESC.ASC) {
                    //sort map by keys in ascending order
                } else {
                    //sort map by keys in descending order
                }
            } else if (orderByString.equals("value")) {
                if (ascOrDESC == AscOrDESC.ASC) {
                    //sort map by values in ascending order
                } else {
                    //sort map by values in descending order
                }
            }
        }



        if (identiferAndFieldReferenceFirst != null && identiferAndFieldReferenceSecond == null)  { //przypisanie wartosci key/value do listy


            for (Map.Entry<Object, Object> entry : dictionary.entrySet()) {
                Map<Object, Object> entryMap = new HashMap<>();

                //where part
                if (whereExpression != null) {
                    IdentiferAndFieldReference whereField = (IdentiferAndFieldReference) whereExpression.getLeft();
                    RelativeType where = whereExpression.getRelativeOperand();
                    SimpleLiteral whereValue = (SimpleLiteral) whereExpression.getRight();

                    if (whereField.getSecondIdentiferName().equals("key")) {
                        if (where == RelativeType.EQUAL) {
                            if (!entry.getKey().equals(whereValue.getValue())) {
                                continue;
                            }
                        } else if (where == RelativeType.NOT_EQUAL) {
                            if (entry.getKey().equals(whereValue.getValue())) {
                                continue;
                            }
                        }
                    } else if (whereField.getSecondIdentiferName().equals("value")) {
                        if (where == RelativeType.EQUAL) {
                            if (!entry.getValue().equals(whereValue.getValue())) {
                                continue;
                            }
                        } else if (where == RelativeType.NOT_EQUAL) {
                            if (entry.getValue().equals(whereValue.getValue())) {
                                continue;
                            }
                        } else if (where == RelativeType.MORE) {
                            if (!(entry.getValue() instanceof Integer) || (int) entry.getValue() <= (int) whereValue.getValue()) {
                                continue;
                            }
                        } else if (where == RelativeType.LESS) {
                            if (!(entry.getValue() instanceof Integer) || (int) entry.getValue() >= (int) whereValue.getValue()) {
                                continue;
                            }
                        } else if (where == RelativeType.MOREEQUAL) {
                            if (!(entry.getValue() instanceof Integer) || (int) entry.getValue() < (int) whereValue.getValue()) {
                                continue;
                            }
                        } else if (where == RelativeType.LESSEQUAL) {
                            if (!(entry.getValue() instanceof Integer) || (int) entry.getValue() > (int) whereValue.getValue()) {
                                continue;
                            }
                        }
                    }
                }



                if (identiferAndFieldReferenceFirst.getSecondIdentiferName().equals("key")) {
                    resultList.add(entry.getKey());
                    listType = typeKey;
                } else if (identiferAndFieldReferenceFirst.getSecondIdentiferName().equals("value")) {
                    resultList.add(entry.getValue());
                    listType = typeValue;
                }

            }






            lastVisitationResult = new VisitationResult(new Variable(Type.LIST, listType, resultList));

        } else if (identiferAndFieldReferenceFirst != null && identiferAndFieldReferenceSecond != null) { //przypisanie wartosci do nowego slownika


            for (Map.Entry<Object, Object> entry : dictionary.entrySet()) {

                //where part
                if (whereExpression != null) {
                    IdentiferAndFieldReference whereField = (IdentiferAndFieldReference) whereExpression.getLeft();
                    RelativeType where = whereExpression.getRelativeOperand();
                    SimpleLiteral whereValue = (SimpleLiteral) whereExpression.getRight();

                    if (whereField.getSecondIdentiferName().equals("key")) { //zakladamy ze dla key mozemy wykonywac jedyne operacje rownosciowe
                        if (where == RelativeType.EQUAL) {
                            if (!entry.getKey().equals(whereValue.getValue())) {
                                continue;
                            }
                        } else if (where == RelativeType.NOT_EQUAL) {
                            if (entry.getKey().equals(whereValue.getValue())) {
                                continue;
                            }
                        }
                    } else if (whereField.getSecondIdentiferName().equals("value")) {
                        if (where == RelativeType.EQUAL) {
                            if (!entry.getValue().equals(whereValue.getValue())) {
                                continue;
                            }
                        } else if (where == RelativeType.NOT_EQUAL) {
                            if (entry.getValue().equals(whereValue.getValue())) {
                                continue;
                            }
                        } else if (where == RelativeType.MORE) {
                            if (!(entry.getValue() instanceof Integer) || (int) entry.getValue() <= (int) whereValue.getValue()) {
                                continue;
                            }
                        } else if (where == RelativeType.LESS) {
                            if (!(entry.getValue() instanceof Integer) || (int) entry.getValue() >= (int) whereValue.getValue()) {
                                continue;
                            }
                        } else if (where == RelativeType.MOREEQUAL) {
                            if (!(entry.getValue() instanceof Integer) || (int) entry.getValue() < (int) whereValue.getValue()) {
                                continue;
                            }
                        } else if (where == RelativeType.LESSEQUAL) {
                            if (!(entry.getValue() instanceof Integer) || (int) entry.getValue() > (int) whereValue.getValue()) {
                                continue;
                            }
                        }
                    }
                }

                if (identiferAndFieldReferenceFirst.getSecondIdentiferName().equals("key") && identiferAndFieldReferenceSecond.getSecondIdentiferName().equals("value")) {
                    resultMap.put(entry.getKey(), entry.getValue());
                } else if (identiferAndFieldReferenceFirst.getSecondIdentiferName().equals("value") && identiferAndFieldReferenceSecond.getSecondIdentiferName().equals("key")) {
                    resultMap.put(entry.getValue(), entry.getKey());
                }

            }

            if (identiferAndFieldReferenceFirst.getSecondIdentiferName().equals("key")) { //przypisanie wartosci key/value do slownika
                lastVisitationResult = new VisitationResult(new Variable(Type.DICTIONARY, typeKey, typeValue, resultMap));
            }
            else {
                lastVisitationResult = new VisitationResult(new Variable(Type.DICTIONARY, typeValue, typeKey, resultMap));
            }



        }
    }

    @Override
    public void visit(IdentifierAndLambdaCall identifierAndLambdaCall) {

    }

    @Override
    public void visit(OrExpression orExpression) throws InterpretingException {
        orExpression.getLeft().accept(this);
        Variable left = lastVisitationResult.getReturnedValue();

        orExpression.getRight().accept(this);
        Variable right = lastVisitationResult.getReturnedValue();

        if (right == null) {
            return;
        }

        boolean result = (boolean) left.getValue() || (boolean) right.getValue();

        lastVisitationResult = new VisitationResult(new Variable(Type.BOOL, result));
    }

    @Override
    public void visit(AndExpression andExpression) throws InterpretingException {
        andExpression.getLeft().accept(this);
        Variable left = lastVisitationResult.getReturnedValue();

        andExpression.getRight().accept(this);
        Variable right = lastVisitationResult.getReturnedValue();

        if (right == null) {
            return;
        }

        boolean result = (boolean) left.getValue() && (boolean) right.getValue();

        lastVisitationResult = new VisitationResult(new Variable(Type.BOOL, result));
    }

    @Override
    public void visit(RelationExpression relationExpression) throws InterpretingException {
        relationExpression.getLeft().accept(this);
        Variable left = lastVisitationResult.getReturnedValue();

        relationExpression.getRight().accept(this);
        Variable right = lastVisitationResult.getReturnedValue();

        if (right == null) {
            return;
        }

        Boolean result = null;
        RelativeType operand = relationExpression.getRelativeOperand();
        switch (operand) {
            case EQUAL:
                result = left.getValue().equals(right.getValue());
                break;
            case NOT_EQUAL:
                result = !left.getValue().equals(right.getValue());
                break;
            case MORE:
                result = compareValues(left.getValue(), right.getValue(), operand);
                break;
            case LESS:
                result = compareValues(left.getValue(), right.getValue(), operand);
                break;
            case MOREEQUAL:
                result = compareValues(left.getValue(), right.getValue(), operand);
                break;
            case LESSEQUAL:
                result = compareValues(left.getValue(), right.getValue(), operand);
                break;
        }
        lastVisitationResult = new VisitationResult(new Variable(Type.BOOL, result));


    }

    private Boolean compareValues(Object left, Object right, RelativeType conditionOperand) throws InterpretingException {
        if (left instanceof Integer && right instanceof Integer) {
            switch (conditionOperand) {
                case MORE:
                    return (int) left > (int) right;
                case MOREEQUAL:
                    return (int) left >= (int) right;
                case LESS:
                    return (int) left < (int) right;
                case LESSEQUAL:
                    return (int) left <= (int) right;
            }
        } else if (left instanceof Float && right instanceof Float) {
            switch(conditionOperand ) {
                case MORE:
                    return (float) left > (float) right;
                case MOREEQUAL:
                    return (float) left >= (float) right;
                case LESS:
                    return (float) left < (float) right;
                case LESSEQUAL:
                    return (float) left <= (float) right;
            }
            return (float) left > (float) right;
        } else if ((left instanceof String && right instanceof String)) {
            String stringLeft = (String) left;
            String stringRight = (String) right;

            int result = stringLeft.compareTo(stringRight);

            if (result < 0) {
                return false;
            } else if (result > 0) {
                return true;
            } else {
                return true; //return true if strings are equal, does not make sense to throw an error here
            }

        }
        throw new InvalidLogicStatementInterpretingException(left, conditionOperand, right);
    }



    @Override
    public void visit(MultiplicativeExpression multiplicativeExpression) throws InterpretingException {
        multiplicativeExpression.getLeft().accept(this);
        Variable left = lastVisitationResult.getReturnedValue();

        multiplicativeExpression.getRight().accept(this);
        Variable right = lastVisitationResult.getReturnedValue();

        if (right == null) {
            return;
        }

        int resultInt;
        float resultFloat;

        MultiplicativeType operand = multiplicativeExpression.getRelativeOperand();
        switch (operand) {
            case MUL:
                if (left.getVariableType() == Type.INT && right.getVariableType() == Type.INT) {
                    resultInt = (int) left.getValue() * (int) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(Type.INT, resultInt));
                } else if (left.getVariableType() == Type.FLOAT && right.getVariableType() == Type.FLOAT) {
                    resultFloat = (float) left.getValue() * (float) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(Type.FLOAT, resultFloat));
                } else {
                    throw new InvalidExpressionInterpretingException(left, right);
                }
                break;
            case DIV:
                if (left.getVariableType() == Type.INT && right.getVariableType() == Type.INT) {
                    if ((int) right.getValue() == 0) {
                        throw new DivisionByZeroInterpretingException();
                    }
                    resultInt = (int) left.getValue() / (int) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(Type.INT, resultInt));
                } else if (left.getVariableType() == Type.FLOAT && right.getVariableType() == Type.FLOAT) {
                    if ((Float) right.getValue() == 0.0f) {
                        throw new DivisionByZeroInterpretingException();
                    }
                    resultFloat = (float) left.getValue() / (float) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(Type.FLOAT, resultFloat));
                } else {
                    throw new InvalidExpressionInterpretingException(left, right);
                }
                break;
        }

    }

    @Override
    public void visit(ArthmeticExpression arthmeticExpression) throws InterpretingException {
        arthmeticExpression.getLeft().accept(this);
        Variable left = lastVisitationResult.getReturnedValue();

        arthmeticExpression.getRight().accept(this);
        Variable right = lastVisitationResult.getReturnedValue();

        if (right == null) {
            return;
        }

        int resultInt;
        float resultFloat;
        String resultString;

        AdditiveType operand = arthmeticExpression.getRelativeOperand();
        switch (operand) {
            case ADD:
                if (left.getVariableType() == Type.INT && right.getVariableType() == Type.INT) {
                    resultInt = (int) left.getValue() + (int) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(Type.INT, resultInt));
                } else if (left.getVariableType() == Type.FLOAT && right.getVariableType() == Type.FLOAT) {
                    resultFloat = (float) left.getValue() + (float) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(Type.FLOAT, resultFloat));
                } else if (left.getVariableType() == Type.STRING && right.getVariableType() == Type.STRING) {
                    resultString = (String) left.getValue() + (String) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(resultString));
                } else {
                    throw new InvalidExpressionInterpretingException(left, right);
                }
                break;
            case SUB:
                if (left.getVariableType() == Type.INT && right.getVariableType() == Type.INT) {
                    resultInt = (int) left.getValue() - (int) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(Type.INT, resultInt));
                } else if (left.getVariableType() == Type.FLOAT && right.getVariableType() == Type.FLOAT) {
                    resultFloat = (float) left.getValue() - (float) right.getValue();
                    lastVisitationResult = new VisitationResult(new Variable(Type.FLOAT, resultFloat));
                } else {
                    throw new InvalidExpressionInterpretingException(left, right);
                }
                break;
        }

    }

    @Override
    public void visit(NegatedExpression negatedExpression) throws InterpretingException {

    negatedExpression.getExpression().accept(this);

    Variable innerExpressionValue = lastVisitationResult.getReturnedValue();

    if (innerExpressionValue.getVariableType() == Type.BOOL) {
        // Negate the boolean value
        boolean negatedValue = !(boolean) innerExpressionValue.getValue();
        lastVisitationResult = new VisitationResult(new Variable(Type.BOOL, negatedValue));
    } else if (innerExpressionValue.getVariableType() == Type.INT) {
        // Negate the integer value
        int negatedValue = -(int) innerExpressionValue.getValue();
        lastVisitationResult = new VisitationResult(new Variable(Type.INT, negatedValue));
    } else if (innerExpressionValue.getVariableType() == Type.FLOAT) {
        // Negate the float value
        float negatedValue = -(float) innerExpressionValue.getValue();
        lastVisitationResult = new VisitationResult(new Variable(Type.FLOAT, negatedValue));
    } else {
        throw new InterpretingException("The negated expression must be of boolean, integer or float type");
    }
}

    @Override
    public void visit(IdentifierExpression identifierExpression) throws NoSuchVariableInterpretingException {
    String variableName = identifierExpression.getName();

    FunctionCallContext currentContext = getLastFunctionCallContext(functionCallContexts);

    Variable variable = currentContext.getVariable(variableName);

    if (variable == null) {
        throw new NoSuchVariableInterpretingException(identifierExpression.getName());
    }

    lastVisitationResult = new VisitationResult(variable);
}

    @Override
    public void visit(CastExpression castExpression) throws InterpretingException {
        // Visit the inner expression
        castExpression.getExpression().accept(this);

        Variable innerExpressionValue = lastVisitationResult.getReturnedValue(); //expression which we want to cast

        Type targetType = castExpression.getType().getType(); //target type

        switch (targetType) {
            case INT:
                if (innerExpressionValue.getVariableType() == Type.FLOAT) {
                    int intValue = (int) (float) innerExpressionValue.getValue();
                 lastVisitationResult = new VisitationResult(new Variable(Type.INT, intValue));
                } else if (innerExpressionValue.getVariableType() == Type.STRING) {
                  try {
                    int intValue = Integer.parseInt((String) innerExpressionValue.getValue());
                    lastVisitationResult = new VisitationResult(new Variable(Type.INT, intValue));
                 } catch (NumberFormatException e) {
                    throw new InterpretingException("Cannot cast string to int: invalid format");
                }
            }
            break;
        case FLOAT:
            if (innerExpressionValue.getVariableType() == Type.INT) {
                // Int to float: lossless conversion
                float floatValue = (float) (int) innerExpressionValue.getValue();
                lastVisitationResult = new VisitationResult(new Variable(Type.FLOAT, floatValue));
            } else if (innerExpressionValue.getVariableType() == Type.STRING) {
                // String to float
                try {
                    float floatValue = Float.parseFloat((String) innerExpressionValue.getValue());
                    lastVisitationResult = new VisitationResult(new Variable(Type.FLOAT, floatValue));
                } catch (NumberFormatException e) {
                    throw new InterpretingException("Cannot cast string to float: invalid format");
                }
            }
            break;
        case STRING:
            // Int or float to string: use the toString method
            String stringValue = innerExpressionValue.getValue().toString();
            lastVisitationResult = new VisitationResult(new Variable(stringValue));
            break;
        default:
            throw new InterpretingException("Invalid target type for cast: " + targetType);
    }
}


    @Override
    public void visit(LiteralTuple literalTuple) {
        SimpleLiteral elementOne = (SimpleLiteral) literalTuple.getObjectOne();  // Retrieve the first element of the tuple
        SimpleLiteral elementTwo = (SimpleLiteral) literalTuple.getObjectTwo();  // Retrieve the second element of the tuple

       Pair pair = new Pair(elementOne.getValue(), elementTwo.getValue());

        lastVisitationResult = new VisitationResult(new Variable(Type.TUPLE, pair));
    }


    @Override
    public void visit(LiteralList literalList) {
        List<Object> values = new ArrayList<>();  // Create a new list to store the extracted values
        for (SimpleLiteral literal : literalList.getValue()) {  // Assuming getLiterals() returns a list of Literal objects
            values.add(literal.getValue());  // Extract the value of each literal and add it to the list
        }
        lastVisitationResult = new VisitationResult(new Variable(Type.LIST, values));
    }

    @Override
    public void visit(LiteralDictionary literalDictionary) {
        Map<Object, Object> newMap = new HashMap<>();
        Map<?, ?> originalMap = literalDictionary.getValue();

        for (Map.Entry<?, ?> entry : originalMap.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (key instanceof SimpleLiteral) {
                key = ((SimpleLiteral) key).getValue();
            }

            if (value instanceof SimpleLiteral) {
                value = ((SimpleLiteral) value).getValue();
            }

            newMap.put(key, value);
        }

        lastVisitationResult = new VisitationResult(new Variable(Type.DICTIONARY, newMap));  // Store the new map in a new Variable
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
        lastVisitationResult = new VisitationResult(new Variable((String) literalString.getValue()));
    }



    public static void assertCorrectReturnedValueType(FunctionCallContext functionCallContext, VisitationResult visitationResult) throws InterpretingException {
        Type functionType = functionCallContext.getFunctionType();
        if ((visitationResult == null || !visitationResult.wasValueReturned() || visitationResult.getReturnedValue() == null) && (functionType != Type.VOID)) {
            throw new NoValueReturnedFromFunctionInterpretingException(functionType.getName());
        }
        Type returnedType = visitationResult.getReturnedValue().getVariableType();
        if ((returnedType != functionType) && (functionType != Type.VOID)) {
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
            Variable nextVar = new Variable(currParam.getType().getType(), currParam.getType().getFirstOptionalParam(), currParam.getType().getSecondOptionalParam(), currParam.getName(), currArg);
            variables.put(nextVar.getName(), nextVar);
        }
        return variables;
    }

}
