package ast;

public class Print extends Statement {
    private Exp printed;

    public Print(Exp printed) {
        this.printed = printed;
    }

    public Exp getPrinted() {
        return printed;
    }

    public void setPrinted(Exp printed) {
        this.printed = printed;
    }

    @Override
    public <C, T> T accept(C context, tinyVarsVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
