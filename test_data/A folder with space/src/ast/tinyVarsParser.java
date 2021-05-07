package ast;

import libs.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class tinyVarsParser {
    private static final String NAME = "[A-Za-z][A-Za-z0-9]*";
    private static final String CONST = "[0-9]+"; // integer constants

    private final Tokenizer tokenizer;

    public static tinyVarsParser getParser(Tokenizer tokenizer) {
        return new tinyVarsParser(tokenizer);
    }

    private tinyVarsParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    //PROGRAM ::= STMT*
    public Program parseProgram() {
        List<Statement> statements = new ArrayList<>();
        while (tokenizer.moreTokens()) {
          statements.add(parseStatement());
        }
        return new Program(statements);
    }

    // STMT    ::= DECLARE | ASSIGN | PRINT | UNDEF
    private Statement parseStatement() {
        if (tokenizer.checkToken("set")) {
            return parseSet();
        }
        else if (tokenizer.checkToken("new")){
            return parseDec();
        }
        else if (tokenizer.checkToken("print")){
            return parsePrint();
        }
        else if (tokenizer.checkToken("undef")){
            return parseUndef();
        }
        else if (tokenizer.checkToken("alias")){
            return parseAlias();
        }
        else {
            throw new RuntimeException("Unknown statement:" + tokenizer.getNext());
        }
    }

    // DECLARE ::= “new” NAME “;”
    private Dec parseDec() {
        tokenizer.getAndCheckNext("new");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext(";");
        return new Dec(name);
    }

    //ASSIGN ::= “set” NAME “,” EXP “;”
    private Set parseSet() {
        tokenizer.getAndCheckNext("set");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext(",");
        Exp exp = parseExp();
        tokenizer.getAndCheckNext(";");
        return new Set(name, exp);
    }


    // Print ::= “print” EXP “;”
    private Print parsePrint() {
        tokenizer.getAndCheckNext("print");
        Exp exp = parseExp();
        tokenizer.getAndCheckNext(";");
        return new Print(exp);
    }

    private Alias parseAlias() {
        tokenizer.getAndCheckNext("alias");
        String newName = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext(",");
        String oldName = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext(";");
        return new Alias(newName, oldName);
    }

    // EXP ::= USE | CONST | … // we assume just USE and CONST here
    // USE ::= NAME  // we'll inline the (simple) rule for USE!
    private Exp parseExp() {
        // we need to choose between two user-defined tokens, here!
        // The simple parsing strategy doesn't quite handle this, but
        // since CONST and NAME can be distinguished from each other,
        // we can just write out the if-conditions:
        if (tokenizer.checkToken(NAME)) {
            return new Name(tokenizer.getNext());
        }
        else if (tokenizer.checkToken(CONST)){
            return new Number(Integer.parseInt(tokenizer.getNext()));
        }
        else {
            throw new RuntimeException("Unknown expression:" + tokenizer.getNext());
        }
    }

    private Undef parseUndef() {
        tokenizer.getAndCheckNext("undef");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext(";");
        return new Undef(name);
    }
}
