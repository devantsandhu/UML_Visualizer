package src.ast;

import java.util.Objects;

public class DVariable {
    String name;
    Visibility visibility;
    boolean isStatic;
    boolean isFinal;
    String typeName;

    public DVariable(String name, Visibility visibility, boolean isStatic, boolean isFinal, String typeName) {
        this.name = name;
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DVariable)) return false;
        DVariable dVariable = (DVariable) o;
        return isStatic() == dVariable.isStatic() &&
                isFinal() == dVariable.isFinal() &&
                Objects.equals(getName(), dVariable.getName()) &&
                getVisibility() == dVariable.getVisibility() &&
                Objects.equals(getTypeName(), dVariable.getTypeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getVisibility(), isStatic(), isFinal(), getTypeName());
    }
}
