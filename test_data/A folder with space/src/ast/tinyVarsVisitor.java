package ast;

public interface tinyVarsVisitor<C,T> {
    // Recall: one visit method per concrete AST node subclass
    T visit(C context, Program p);
    T visit(C context, Dec d);
    T visit(C context, Set s);
    T visit(C context, Print p);
    T visit(C context, Name n);
    T visit(C context, Number n);
    T visit(C context, Undef u);
    T visit(C context, Alias a);
}
