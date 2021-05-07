package ast;

public class Number extends Exp {
    private int value;

    public Number(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    @Override
    public <C, T> T accept(C context, tinyVarsVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
