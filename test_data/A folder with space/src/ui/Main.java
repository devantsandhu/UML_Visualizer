package ui;

import ast.*;
import libs.SimpleTokenizer;
import libs.Tokenizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<String> fixedLiterals = Arrays.asList("set", "print", "new", "undef", "alias", ",", ";");
        Tokenizer tokenizer = SimpleTokenizer.createSimpleTokenizer("input.tvar",fixedLiterals);
        System.out.println("Done tokenizing");

        tinyVarsParser p = tinyVarsParser.getParser(tokenizer);
        Program program = p.parseProgram();
        System.out.println("Done parsing");
        System.out.println("Done parsing");

        tinyVarsEvaluator e = new tinyVarsEvaluator();
        program.accept(null,e);
        System.out.println("completed successfully");
        System.out.println("Final Environment: " + e.getEnvironment());
        System.out.println("Final Memory: " + e.getMemory());
    }

}
