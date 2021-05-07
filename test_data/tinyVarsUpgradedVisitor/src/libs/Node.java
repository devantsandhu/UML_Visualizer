package libs;

import ast.tinyVarsVisitor;

import java.io.PrintWriter;

public abstract class Node {
    abstract public <C, T> T accept(C context, tinyVarsVisitor<C, T> v);
}
