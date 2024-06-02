package org.example.interpreter;

import org.example.interpreter.error.InterpretingException;
import org.example.parser.Structure.OtherComponents.Program;

public class Interpreter {
    private final Program program;
    private final InterpretingVisitor interpretingVisitor;

    public Interpreter(Program program) {
        this.program = program;
        interpretingVisitor = new InterpretingVisitor(program);
    }

    public Object execute() throws InterpretingException {
        Object interpretingResult = interpretingVisitor.getInterpretingResult();
        System.out.println("Program ends with result: " + interpretingResult);
        return interpretingResult;
    }

    }

