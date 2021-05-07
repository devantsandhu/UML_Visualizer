package ast;

import libs.Node;

import java.util.ArrayList;
import java.util.List;

public class Program extends Node{
    private List<Statement> statements = new ArrayList<>();

    public List<Statement> getStatements() {
        return statements;
    }

    public Program(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <C, T> T accept(C context, tinyVarsVisitor<C, T> v) {
        return v.visit(context,this);
    }

}
