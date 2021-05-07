package ast;

public class Alias extends Statement {
    private String newName;
    private String oldName;

    public Alias(String newName, String oldName) {
        this.newName = newName;
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @Override
    public <C, T> T accept(C context, tinyVarsVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
