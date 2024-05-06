package org.example.parser;

import org.example.parser.Structure.Argument;
import org.example.parser.Structure.FunctionDefinition;
import org.example.parser.Structure.Program;

public interface Visitor {
    void visit(Program program);
    void visit(FunctionDefinition functionDefinition);

    void visit(Argument argument);
}
