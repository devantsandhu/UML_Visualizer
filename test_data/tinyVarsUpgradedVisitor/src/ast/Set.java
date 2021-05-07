package ast;

import ui.Main;

public class Set extends Statement {
    private String name;
    private Exp exp;

    public Set(String name, Exp exp) {
        this.name = name;
        this.exp = exp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Exp getExp() {
        return exp;
    }

    public void setExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public <C, T> T accept(C context, tinyVarsVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
