package src.ast;

public class DFunction {
    private String name;
    private Visibility visibility; // is one of isPublic, isPrivate, isProtected
    private boolean isStatic;
    private boolean isAbstract;

    public DFunction(String name, Visibility visibility, boolean isStatic, boolean isAbstract) {
        this.name = name;
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
    }

    public DFunction(String name) {
        this.name = name;
        this.visibility = Visibility.isPublic;
        this.isStatic = false;
        this.isAbstract = false;
    }

    public String getName() {
        return name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DFunction)) return false;
        DFunction fun = (DFunction) obj;
        return getName().equals(fun.getName()) &&
                getVisibility().equals(fun.getVisibility()) &&
                isStatic() == fun.isStatic() &&
                isAbstract == fun.isAbstract();

    }
}
