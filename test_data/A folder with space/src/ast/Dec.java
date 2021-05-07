package ast;

import ui.Main;

public class Dec extends Statement {
    private String name;

    public Dec(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <C, T> T accept(C context, tinyVarsVisitor<C, T> v) {
        return v.visit(context,this);
    }

}
