package ast;

import ui.Main;

import java.util.HashMap;
import java.util.Map;

public class tinyVarsEvaluator implements tinyVarsVisitor<Void, Integer> {
    private final Map<String, Integer> environment = new HashMap<>(); // locations of variables
    private final Map<Integer, Integer> memory = new HashMap<>(); // values at locations

    public Map<String, Integer> getEnvironment() {
        return environment;
    }

    public Map<Integer, Integer> getMemory() {
        return memory;
    }

    private Integer getFreshLocation() {
        Integer i = Integer.valueOf(0); // or use an int; it will get boxed when needed
        while (environment.containsValue(i)) { // Q. what difference might it make if we used memory.containsKey(i) instead?
            i = i + 1; // unboxing and boxing is automatic
        }
        return i;
    }

    @Override
    public Integer visit(Void v, Program p) {
        for (Statement s : p.getStatements()) {
            s.accept(null, this);
        }
        return null; // we only return a value for expressions (EXP); evaluation of programs/statements is via side-effects
    }

    @Override
    public Integer visit(Void v, Dec d) {
        System.out.println("Putting " + d.getName() + " into symbol table");
        environment.put(d.getName(), getFreshLocation()); // no value yet
        return null; // we only return a value for expressions (EXP); evaluation of statements is via side-effects
    }

    @Override
    public Integer visit(Void v, Undef u) {
        System.out.println("Removing " + u.getName() + " into symbol table");
        environment.remove(u.getName());
        return null; // we only return a value for expressions (EXP); evaluation of statements is via side-effects
    }

    @Override
    public Integer visit(Void v, Set s) {
        System.out.println("Evaluating " + s.getExp());
        Integer result = s.getExp().accept(null, this);
        System.out.println("Setting " + s.getName() + " to " + result);

        memory.put(environment.get(s.getName()), result);
        return null; // we only return a value for expressions (EXP); evaluation of statements is via side-effects
    }

    @Override
    public Integer visit(Void v, Alias a) {
        System.out.println("Creating alias " + a.getNewName() + " for " + a.getOldName());

        environment.put(a.getNewName(), environment.get(a.getOldName())); // same location
        return null; // we only return a value for expressions (EXP); evaluation of statements is via side-effects
    }

    @Override
    public Integer visit(Void v, Print p) {
        System.out.println("PRINTING: " + p.getPrinted().accept(null, this));
        return null; // we only return a value for expressions (EXP); evaluation of statements is via side-effects
    }

    @Override
    public Integer visit(Void v, Name n) {
        return memory.get(environment.get(n.getName())); // lookup: variable -> location -> value
    }

    @Override
    public Integer visit(Void v, Number n) {
        return n.getValue(); // Java auto-boxes this int as an Integer object
    }
}
