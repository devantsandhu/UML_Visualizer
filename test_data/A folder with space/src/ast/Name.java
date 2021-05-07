package ast;

import ui.Main;

public class Name extends Exp {
    private String name;

    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public <C, T> T accept(C context, tinyVarsVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
