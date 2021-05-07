package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.ast.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class parserTest {
    List<DClass> referenceData;

    //This reference data so far only includes name.
    //TODO add all child fields except body to each DClass
    @BeforeEach
    void createReferenceData() {
        ArrayList<DClass> out = new ArrayList<>();
        DClass Alias = new DClass("Alias", null);
        DClass Dec = new DClass("Dec", null);
        DClass Exp = new DClass("Exp", null);
        DClass Name = new DClass("Name", null);
        DClass Number = new DClass("Number", null);
        DClass Print = new DClass("Print", null);
        DClass Program = new DClass("Program", null);
        DClass Set = new DClass("Set", null);
        DClass Statement = new DClass("Statement", null);
        DClass TinyVarsChecker = new DClass("TinyVarsChecker", null);
        DClass TinyVarsEvaluator = new DClass("tinyVarsEvaluator", null);
        DClass TinyVarsParser = new DClass("tinyVarsParser", null);
        DClass TinyVarsVisitor = new DClass("tinyVarsVisitor", null);
        DClass Undef = new DClass("Undef", null);
        DClass Node = new DClass("Node", null);
        DClass SimpleTokenizer = new DClass("SimpleTokenizer", null);
        DClass Tokenizer = new DClass("Tokenizer", null);
        DClass Main = new DClass("Main", null);

        Alias.setPackageName("ast");
        Alias.setExtendsClass(Statement);
        Alias.addUseClass(TinyVarsVisitor);
        Alias.addFunction(new DFunction("getNewName"));
        Alias.addFunction(new DFunction("setNewName"));
        Alias.addFunction(new DFunction("getOldName"));
        Alias.addFunction(new DFunction("setOldName"));
        Alias.addFunction(new DFunction("accept"));
        Alias.addVariable(new DVariable("newName", Visibility.isPrivate, false, false, "String"));
        Alias.addVariable(new DVariable("oldName", Visibility.isPrivate, false, false, "String"));

        Dec.setPackageName("ast");
        Dec.setExtendsClass(Statement);
        Dec.addUseClass(TinyVarsVisitor);
        Dec.addFunction(new DFunction("getName"));
        Dec.addFunction(new DFunction("setName"));
        Dec.addFunction(new DFunction("accept"));
        Dec.addVariable(new DVariable("name", Visibility.isPrivate, false, false, "String"));

        Exp.setPackageName("ast");
        Exp.setExtendsClass(Node);
        Exp.setAbstract(true);

        Name.setPackageName("ast");
        Name.setExtendsClass(Exp);
        Name.addUseClass(TinyVarsVisitor);
        Name.addFunction(new DFunction("getName"));
        Name.addFunction(new DFunction("setName"));
        Name.addFunction(new DFunction("toString"));
        Name.addFunction(new DFunction("accept"));
        Name.addVariable(new DVariable("name", Visibility.isPrivate, false, false, "String"));

        Number.setPackageName("ast");
        Number.setExtendsClass(Exp);
        Number.addUseClass(TinyVarsVisitor);
        Number.addFunction(new DFunction("getValue"));
        Number.addFunction(new DFunction("setValue"));
        Number.addFunction(new DFunction("toString"));
        Number.addFunction(new DFunction("accept"));
        Number.addVariable(new DVariable("value", Visibility.isPrivate, false, false, "int"));

        Print.setPackageName("ast");
        Print.setExtendsClass(Statement);
        Print.addUseClass(Exp);
        Print.addUseClass(TinyVarsVisitor);
        Print.addFunction(new DFunction("getPrinted"));
        Print.addFunction(new DFunction("setPrinted"));
        Print.addFunction(new DFunction("accept"));
        Print.addVariable(new DVariable("printed", Visibility.isPrivate, false, false, "Exp"));

        Program.setPackageName("ast");
        Program.setExtendsClass(Node);
        Program.addUseClass(Statement);
        Program.addUseClass(TinyVarsVisitor);
        Program.addFunction(new DFunction("getStatements"));
        Program.addFunction(new DFunction("accept"));
        Program.addVariable(new DVariable("statements", Visibility.isPrivate, false, false, "List<Statement>"));

        Set.setPackageName("ast");
        Set.setExtendsClass(Statement);
        Set.addUseClass(Exp);
        Set.addUseClass(TinyVarsVisitor);
        Set.addFunction(new DFunction("getName"));
        Set.addFunction(new DFunction("setName"));
        Set.addFunction(new DFunction("getExp"));
        Set.addFunction(new DFunction("setExp"));
        Set.addFunction(new DFunction("accept"));
        Set.addVariable(new DVariable("name", Visibility.isPrivate, false, false, "String"));
        Set.addVariable(new DVariable("exp", Visibility.isPrivate, false, false, "Exp"));

        Statement.setPackageName("ast");
        Statement.setExtendsClass(Node);
        Statement.setAbstract(true);

        TinyVarsChecker.setPackageName("ast");
        TinyVarsChecker.setImplementsClass(TinyVarsVisitor);

        TinyVarsEvaluator.setPackageName("ast");
        TinyVarsEvaluator.setImplementsClass(TinyVarsVisitor);
        TinyVarsEvaluator.addUseClass(Program);
        TinyVarsEvaluator.addUseClass(Statement);
        TinyVarsEvaluator.addUseClass(Dec);
        TinyVarsEvaluator.addUseClass(Undef);
        TinyVarsEvaluator.addUseClass(Set);
        TinyVarsEvaluator.addUseClass(Alias);
        TinyVarsEvaluator.addUseClass(Print);
        TinyVarsEvaluator.addUseClass(Name);
        TinyVarsEvaluator.addUseClass(Number);
        TinyVarsEvaluator.addFunction(new DFunction("getEnvironment"));
        TinyVarsEvaluator.addFunction(new DFunction("getMemory"));
        TinyVarsEvaluator.addFunction(new DFunction("visit"));
        TinyVarsEvaluator.addFunction(new DFunction("visit"));
        TinyVarsEvaluator.addFunction(new DFunction("visit"));
        TinyVarsEvaluator.addFunction(new DFunction("visit"));
        TinyVarsEvaluator.addFunction(new DFunction("visit"));
        TinyVarsEvaluator.addFunction(new DFunction("visit"));
        TinyVarsEvaluator.addFunction(new DFunction("visit"));
        TinyVarsEvaluator.addFunction(new DFunction("visit"));
        TinyVarsEvaluator.addVariable(new DVariable("environment", Visibility.isPrivate, false, true, "Map<String, Integer>"));
        TinyVarsEvaluator.addVariable(new DVariable("memory", Visibility.isPrivate, false, true, "Map<Integer, Integer>"));

        TinyVarsParser.setPackageName("ast");
        TinyVarsParser.addUseClass(Tokenizer);
        TinyVarsParser.addUseClass(Program);
        TinyVarsParser.addUseClass(Statement);
        TinyVarsParser.addUseClass(Dec);
        TinyVarsParser.addUseClass(Set);
        TinyVarsParser.addUseClass(Exp);
        TinyVarsParser.addUseClass(Print);
        TinyVarsParser.addUseClass(Alias);
        TinyVarsParser.addUseClass(Name);
        TinyVarsParser.addUseClass(Number);
        TinyVarsParser.addUseClass(Undef);
        TinyVarsParser.addFunction(new DFunction("getParser", Visibility.isPublic, true, false));
        TinyVarsParser.addFunction(new DFunction("parseProgram"));
        TinyVarsParser.addVariable(new DVariable("NAME", Visibility.isPrivate, true, true, "String"));
        TinyVarsParser.addVariable(new DVariable("CONST", Visibility.isPrivate, true, true, "String"));
        TinyVarsParser.addVariable(new DVariable("tokenizer", Visibility.isPrivate, false, true, "Tokenizer"));

        TinyVarsVisitor.setPackageName("ast");
        TinyVarsVisitor.setInterface(true);
        TinyVarsVisitor.addUseClass(Program);
        TinyVarsVisitor.addUseClass(Dec);
        TinyVarsVisitor.addUseClass(Set);
        TinyVarsVisitor.addUseClass(Print);
        TinyVarsVisitor.addUseClass(Name);
        TinyVarsVisitor.addUseClass(Number);
        TinyVarsVisitor.addUseClass(Undef);
        TinyVarsVisitor.addUseClass(Alias);
        TinyVarsVisitor.addFunction(new DFunction("visit", Visibility.isDefault, false, false));
        TinyVarsVisitor.addFunction(new DFunction("visit", Visibility.isDefault, false, false));
        TinyVarsVisitor.addFunction(new DFunction("visit", Visibility.isDefault, false, false));
        TinyVarsVisitor.addFunction(new DFunction("visit", Visibility.isDefault, false, false));
        TinyVarsVisitor.addFunction(new DFunction("visit", Visibility.isDefault, false, false));
        TinyVarsVisitor.addFunction(new DFunction("visit", Visibility.isDefault, false, false));
        TinyVarsVisitor.addFunction(new DFunction("visit", Visibility.isDefault, false, false));
        TinyVarsVisitor.addFunction(new DFunction("visit", Visibility.isDefault, false, false));

        Undef.setPackageName("ast");
        Undef.setExtendsClass(Statement);
        Undef.addUseClass(TinyVarsVisitor);
        Undef.addFunction(new DFunction("getName"));
        Undef.addFunction(new DFunction("setName"));
        Undef.addFunction(new DFunction("accept"));
        Undef.addVariable(new DVariable("name", Visibility.isPrivate, false, false, "String"));

        Node.setPackageName("libs");
        Node.setAbstract(true);
        Node.addUseClass(TinyVarsVisitor);
        Node.addFunction(new DFunction("accept", Visibility.isPublic, false, true));

        SimpleTokenizer.setPackageName("libs");
        SimpleTokenizer.setImplementsClass(Tokenizer);
        SimpleTokenizer.addUseClass(Tokenizer);
        SimpleTokenizer.addFunction(new DFunction("createSimpleTokenizer", Visibility.isPublic, true, false));
        SimpleTokenizer.addFunction(new DFunction("getNext"));
        SimpleTokenizer.addFunction(new DFunction("checkToken"));
        SimpleTokenizer.addFunction(new DFunction("getAndCheckNext"));
        SimpleTokenizer.addFunction(new DFunction("moreTokens"));
        SimpleTokenizer.addVariable(new DVariable("RESERVEDWORD", Visibility.isPrivate, true, true, "String"));
        SimpleTokenizer.addVariable(new DVariable("inputProgram", Visibility.isPrivate, false, false, "String"));
        SimpleTokenizer.addVariable(new DVariable("fixedLiterals", Visibility.isPrivate, false, false, "List<String>"));
        SimpleTokenizer.addVariable(new DVariable("tokens", Visibility.isPrivate, false, false, "String[]"));
        SimpleTokenizer.addVariable(new DVariable("currentToken", Visibility.isPrivate, false, false, "int"));

        Tokenizer.setPackageName("libs");
        Tokenizer.setInterface(true);
        Tokenizer.addFunction(new DFunction("getNext", Visibility.isDefault, false, false));
        Tokenizer.addFunction(new DFunction("checkToken", Visibility.isDefault, false, false));
        Tokenizer.addFunction(new DFunction("getAndCheckNext", Visibility.isDefault, false, false));
        Tokenizer.addFunction(new DFunction("moreTokens", Visibility.isDefault, false, false));

        Main.setPackageName("ui");
        Main.addUseClass(Tokenizer);
        Main.addUseClass(SimpleTokenizer);
        Main.addUseClass(TinyVarsParser);
        Main.addUseClass(Program);
        Main.addUseClass(TinyVarsEvaluator);
        Main.addFunction(new DFunction("main", Visibility.isPublic, true, false));


        out.add(Alias);
        out.add(Dec);
        out.add(Exp);
        out.add(Name);
        out.add(Number);
        out.add(Print);
        out.add(Program);
        out.add(Set);
        out.add(Statement);
        out.add(TinyVarsChecker);
        out.add(TinyVarsEvaluator);
        out.add(TinyVarsParser);
        out.add(TinyVarsVisitor);
        out.add(Undef);
        out.add(Node);
        out.add(SimpleTokenizer);
        out.add(Tokenizer);
        out.add(Main);

        this.referenceData = out;
    }

    //Tests that program can handle an invalid input: no input
    @Test
    void testNoInput() {
        String path = "";
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            assertNotNull(classes);
            assertEquals(classes.size(), 0);
        } catch (Exception e) {
            fail();
        }
    }

    //Tests that program can handle an invalid input: empty folder
    @Test
    void testEmptyFolder() {
        String path = "./test_data/anEmptyFolder";
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            assertNotNull(classes);
            assertEquals(classes.size(), 0);
        } catch (Exception e) {
            fail();
        }
    }

    //Tests that program can handle an invalid input: no java files
    @Test
    void testFolderDoesntContainJavaFiles() {
        String path = "./test_data/aFolderWithoutJavaFiles";
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            assertNotNull(classes);
            assertEquals(classes.size(), 0);
        } catch (Exception e) {
            fail();
        }
    }

    //Tests that all classes were read and have the correct name but does not check their fields or body
    @Test
    void testConstructorParsesFiles() {
        String path = "./test_data/tinyVarsUpgradedVisitor/src";
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            assertNotNull(classes);
            assertEquals(classes.size(), referenceData.size());
            for (int i = 0; i < classes.size(); i++) {
                assertEquals(classes.get(i).getName(), referenceData.get(i).getName());
            }
        } catch (Exception e) {
            fail();
        }
    }

    //Tests that the program correctly reads packages
    @Test
    void testPackage() {
        String path = "./test_data/tinyVarsUpgradedVisitor/src";
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            assertNotNull(classes);
            assertEquals(classes.size(), referenceData.size());
            for (int i = 0; i < classes.size(); i++) {
                assertEquals(classes.get(i).getPackageName(), referenceData.get(i).getPackageName());
            }
        } catch (Exception e) {
            fail();
        }
    }

    //Tests that all class fields were correctly instantiated
    @Test
    void testBodyRemovesComments() {
        List<String> referenceBody = new LinkedList<>();
        referenceBody.add("package ast;");
        referenceBody.add("import java.util.HashMap;");
        referenceBody.add("public class TinyVarsChecker implements tinyVarsVisitor<HashMap<String,Boolean>, String> {");
        referenceBody.add("}");
        String path = "./test_data/multiline_comment";
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            DClass dClass = classes.get(0);
            List<String> body = dClass.getBody();
            for (int i = 0; i < referenceBody.size(); i++) {
                assertEquals(body.get(i), referenceBody.get(i));
            }
        } catch (Exception e) {
            fail();
        }
    }

    //Tests that all class fields were correctly instantiated
    @Test
    void testFullParseOnTinyVars() {
        String path = "./test_data/tinyVarsUpgradedVisitor/src";
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            assertNotNull(classes);
            assertEquals(classes.size(), referenceData.size());
            for (int i = 0; i < classes.size(); i++) {
                if (!classes.get(i).equals(referenceData.get(i))) {
                    System.err.println("Output incorrect");
                    classes.get(i).equals(referenceData.get(i));
                    fail();
                }
            }
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testCombineLinesThatAreSplit() {
        String path = "test_data/splitLines";
        List<String> referenceBody = new LinkedList<>();
        referenceBody.add("package ast;");
        referenceBody.add("import java.util.HashMap;");
        referenceBody.add("public class TinyVarsChecker implements tinyVarsVisitor<HashMap<String,Boolean>, String> {");
        referenceBody.add("System.out.println(*~^*~*^~*);");
        referenceBody.add("}");
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            DClass dClass = classes.get(0);
            assertNotNull(classes);
            List<String> body = dClass.getBody();
            assertEquals(classes.size(), 1);
            assertEquals(body.size(), referenceBody.size());
            for (int i = 0; i < classes.size(); i++) {
                assertEquals(body.get(i), referenceBody.get(i));
            }
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testMultipleBracketsSameLine() {
        String path = "test_data/multipleCurlies";
        List<String> referenceBody = new LinkedList<>();
        referenceBody.add("package ast;");
        referenceBody.add("import java.util.HashMap;");
        referenceBody.add("public class TinyVarsChecker implements tinyVarsVisitor<HashMap<String,Boolean>, String> {");
        referenceBody.add("{");
        referenceBody.add("{System.out.println(*~^*~*^~*);}");
        referenceBody.add("}");
        referenceBody.add("}");
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Parser p = new Parser(paths);
        try {
            List<DClass> classes = p.GetClassHierarchy();
            DClass dClass = classes.get(0);
            List<String> body = dClass.getBody();
            assertNotNull(classes);
            assertEquals(classes.size(), 1);
            assertEquals(body.size(), referenceBody.size());
            for (int i = 0; i < classes.size(); i++) {
                assertEquals(body.get(i), referenceBody.get(i));
            }
        } catch (Exception e) {
            fail();
        }
    }
}
