package src.ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DClass {
    private String name;
    private Visibility visibility;
    private String packageName;
    private boolean isAbstract = false;
    private boolean isInterface = false;
    private boolean isEnum = false;
    private DClass implementsClass;
    private DClass extendsClass;
    private List<DClass> useClass;
    private List<DFunction> functions;
    private List<DVariable> variables;
    private LinkedList<String> body; // un-parsed body of the class.

    public DClass(String name, LinkedList<String> body) {
        this.name = name;
        this.body = body;
        this.useClass = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.visibility = Visibility.isPublic; //setting default class to public not default since it is most common case
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public void setEnum(boolean anEnum) {
        isEnum = anEnum;
    }

    public DClass getImplementsClass() {
        return implementsClass;
    }

    public void setImplementsClass(DClass implementsClass) {
        this.implementsClass = implementsClass;
    }

    public DClass getExtendsClass() {
        return extendsClass;
    }

    public void setExtendsClass(DClass extendsClass) {
        this.extendsClass = extendsClass;
    }

    public List<DClass> getUseClass() {
        return useClass;
    }

    public void setUseClass(List<DClass> useClass) {
        this.useClass = useClass;
    }

    public void addUseClass(DClass useClass) {
        this.useClass.add(useClass);
    }

    public boolean doesUseClassExistAlready(DClass useClass) {
        return this.useClass.contains(useClass);
    }

    public List<DFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<DFunction> functions) {
        this.functions = functions;
    }

    public void addFunction(DFunction function) {
        this.functions.add(function);
    }

    public boolean doesFunctionExistAlready(DFunction function) {
        return this.functions.contains(function);
    }

    public LinkedList<String> getBody() {
        return body;
    }

    public void setBody(LinkedList<String> body) {
        this.body = body;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public List<DVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<DVariable> variables) {
        this.variables = variables;
    }

    public void addVariable(DVariable variable) {
        this.variables.add(variable);
    }

    public boolean doesVariableExistAlready(DFunction variable) {
        return this.variables.contains(variable);
    }

    //To avoid infinite recursion we must not compare all parameters
    private boolean crappyEquals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DClass)) return false;
        DClass dClass = (DClass) o;
        return getName().equals(dClass.getName()) && getPackageName().equals(dClass.getPackageName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DClass)) return false;
        DClass dClass = (DClass) o;
        boolean listsEqual;
        listsEqual = (getUseClass().size() == dClass.getUseClass().size()) &&
                (getFunctions().size() == dClass.getFunctions().size()) &&
                (getVariables().size() == dClass.getVariables().size());
        if (!listsEqual) return false;
        for (int i = 0; i < getUseClass().size(); i++) {
            listsEqual = listsEqual && (getUseClass().get(i).crappyEquals(dClass.getUseClass().get(i)));
        }
        for (int i = 0; i < getFunctions().size(); i++) {
            listsEqual = listsEqual && (getFunctions().get(i).equals(dClass.getFunctions().get(i)));
        }
        for (int i = 0; i < getVariables().size(); i++) {
            listsEqual = listsEqual && (getVariables().get(i).equals(dClass.getVariables().get(i)));
        }
        return listsEqual &&
                isAbstract() == dClass.isAbstract() &&
                isInterface() == dClass.isInterface() &&
                isEnum() == dClass.isEnum() &&
                getName().equals(dClass.getName()) &&
                Objects.equals(getVisibility(), dClass.getVisibility()) &&
                Objects.equals(getPackageName(), dClass.getPackageName()) &&
                Objects.equals(getImplementsClass(), dClass.getImplementsClass()) &&
                Objects.equals(getExtendsClass(), dClass.getExtendsClass());
    }

    public void printBody() {
        System.out.println();
        for (String line : body) {
            System.out.println(line);
        }
        System.out.println();
    }
}
