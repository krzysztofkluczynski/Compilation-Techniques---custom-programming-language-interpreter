package org.example.interpreter;

import org.example.interpreter.error.*;
import org.example.interpreter.model.*;
import org.example.parser.Enum.AdditiveType;
import org.example.parser.Enum.MultiplicativeType;
import org.example.parser.Enum.RelativeType;
import org.example.parser.Enum.Type;
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
    public void visit(ConditionalStatement conditionalStatement) {

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


//        if (declarationStatement.getType().getType() == Type.TUPLE ) {
//            Type firstOptionalType = declarationStatement.getType().getFirstOptionalParam();
//            Type secondOptionalType = declarationStatement.getType().getSecondOptionalParam();
//            lastVisitationResult = new VisitationResult(
//                    new Variable(Type.TUPLE, firstOptionalType, secondOptionalType, declarationStatement.getName()));
//
//        } else if (declarationStatement.getType().getType() == Type.DICTIONARY) {
//            Type firstOptionalType = declarationStatement.getType().getFirstOptionalParam();
//            Type secondOptionalType = declarationStatement.getType().getSecondOptionalParam();
//            lastVisitationResult = new VisitationResult(
//                    new Variable(Type.DICTIONARY, firstOptionalType, secondOptionalType, declarationStatement.getName()));
//
//        } else if (declarationStatement.getType().getType() == Type.LIST) {
//            Type firstOptionalType = declarationStatement.getType().getFirstOptionalParam();
//            lastVisitationResult = new VisitationResult(
//                    new Variable(Type.LIST, firstOptionalType, declarationStatement.getName()));
//
//        } else {
//            lastVisitationResult = new VisitationResult(
//                    new Variable(declarationStatement.getType().getType(), declarationStatement.getName()));
//        }


        lastVisitationResult = new VisitationResult(
                new Variable(declarationStatement.getType().getType(), declarationStatement.getName()));

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
    public void visit(DefinitionWithQueryStatement definitionWithQueryStatement) {

    }

    @Override
    public void visit(AssignmentWithQueryStatement assignmentWithQueryStatement) throws NoSuchVariableInterpretingException {
//TODO
    }

    @Override
    public void visit(IdentiferAndMethodCallExpression identiferAndMethodCallExpression) {

    }


    @Override
    public void visit(IdentifierAndLambdaCall identifierAndLambdaCall) {

    }





//    @Override
//    public void visit(If anIf) {
//
//    }



    @Override
    public void visit(QueryExpression queryExpression) {

    }

    @Override
    public void visit(IdentiferAndFieldReference identiferAndFieldReference) {

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
                result = getLeftMoreThenRight(left.getValue(), right.getValue(), operand);
                break;
            case LESS:
                result = !getLeftMoreThenRight(left.getValue(), right.getValue(), operand);
                break;
        }
        lastVisitationResult = new VisitationResult(new Variable(Type.BOOL, result));

    }

    private Boolean getLeftMoreThenRight(Object left, Object right, RelativeType conditionOperand) throws InterpretingException {
        if (left instanceof Integer && right instanceof Integer) {
            return (int) left > (int) right;
        } else if (left instanceof Float && right instanceof Float) {
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
