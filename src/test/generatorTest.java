package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.fail;

public class generatorTest {

    private DClass interfaceObject;
    private DClass abstractClassObject;
    private DClass classObject;
    private ArrayList<DClass> testData;
    private Params defaultParams;

    private String parseClassAndGetOutput(Params flags) throws IOException {
        PlantUMLGenerator t = new PlantUMLGenerator("test", flags);
        t.testParseClasses(testData);
        return t.getOutputString();
    }

    @BeforeEach
    void setupTestData() {
        testData = new ArrayList<>();
        interfaceObject = new DClass("Interface", null);
        abstractClassObject = new DClass("AbstractClass", null);
        classObject = new DClass("Class", null);
        defaultParams = new Params("-r",null);
        interfaceObject.setInterface(true);
        abstractClassObject.setAbstract(true);
        testData.add(interfaceObject);
        testData.add(abstractClassObject);
        testData.add(classObject);
    }

    @Test
    void testClassCreation() {
        DClass enumObject = new DClass("enumClass", null);
        enumObject.setEnum(true);
        testData.add(enumObject);

        String expectedOutput = "@startuml\n" +
                                "interface " + interfaceObject.getName() + " {\n" +
                                "--\n" +
                                "}\n" +
                                "abstract class " + abstractClassObject.getName() + " {\n" +
                                "--\n" +
                                "}\n" +
                                "class " + classObject.getName() + " {\n" +
                                "--\n" +
                                "}\n" +
                                "enum " + enumObject.getName() + " {\n" +
                                "--\n" +
                                "}\n" +
                                "@enduml";

        try {
            String output = parseClassAndGetOutput(defaultParams);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testImplements() {
        classObject.setImplementsClass(interfaceObject);

        String expectedOutput = "@startuml\n" +
                                "interface " + interfaceObject.getName() + " {\n" +
                                "--\n" +
                                "}\n" +
                                "abstract class " + abstractClassObject.getName() + " {\n" +
                                "--\n" +
                                "}\n" +
                                "class " + classObject.getName() + " {\n" +
                                "--\n" +
                                "}\n" +
                                classObject.getName() + " ..|> " + classObject.getImplementsClass().getName() +"\n\n" +
                                "@enduml";

        try {
            String output = parseClassAndGetOutput(defaultParams);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    void testExtend() {
        classObject.setExtendsClass(abstractClassObject);
        String expectedOutput = "@startuml\n" +
                "interface " + interfaceObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "abstract class " + abstractClassObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "class " + classObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                classObject.getName() + " --|> " + classObject.getExtendsClass().getName() +"\n\n" +
                "@enduml";

        try {
            String output = parseClassAndGetOutput(defaultParams);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    void testUse() {
        DClass classObject2 = new DClass("classObject2", null);
        testData.add(classObject2);
        ArrayList<DClass> useClasses = new ArrayList<>();
        useClasses.add(abstractClassObject);
        useClasses.add(classObject2);
        classObject.setUseClass(useClasses);
        String expectedOutput = "@startuml\n" +
                "interface " + interfaceObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "abstract class " + abstractClassObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "class " + classObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "class " + classObject2.getName() + " {\n" +
                "--\n" +
                "}\n" +
                classObject.getName() + " ..> " + abstractClassObject.getName() +"\n\n" +
                classObject.getName() + " ..> " + classObject2.getName() + "\n\n" +
                "@enduml";

        try {
            String output = parseClassAndGetOutput(defaultParams);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testFunctionAndVariables() {
        DVariable privateVar = new DVariable("privateVar", Visibility.isPrivate, false, false, "String");
        DVariable publicVar = new DVariable("publicVar", Visibility.isPublic, false, false, "String");
        DVariable protectedVar = new DVariable("protectedVar", Visibility.isProtected, false, false, "String");

        DFunction privateFunc = new DFunction("privateFunc", Visibility.isPrivate, false, false);
        DFunction publicFunc = new DFunction("publicFunc", Visibility.isPublic, false, false);
        DFunction protectedFunc = new DFunction("protectedFUnc", Visibility.isProtected, false, false);

        classObject.addVariable(privateVar);
        classObject.addVariable(publicVar);
        classObject.addVariable(protectedVar);

        abstractClassObject.addFunction(privateFunc);
        abstractClassObject.addFunction(publicFunc);
        abstractClassObject.addFunction(protectedFunc);

        String expectedOutput = "@startuml\n" +
                "interface " + interfaceObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "abstract class " + abstractClassObject.getName() + " {\n" +
                "--\n" +
                "- " + privateFunc.getName() + "\n" +
                "+ " + publicFunc.getName() + "\n" +
                "# " + protectedFunc.getName() + "\n" +
                "}\n" +
                "class " + classObject.getName() + " {\n" +
                "- " + privateVar.getName() + "\n" +
                "+ " + publicVar.getName() + "\n" +
                "# " + protectedVar.getName() + "\n" +
                "--\n" +
                "}\n" +
                "@enduml";

        try {
            String output = parseClassAndGetOutput(defaultParams);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testFunctionAndVariablesMaxLength() {
        DVariable privateVar = new DVariable("privateVar", Visibility.isPrivate, false, false, "String");
        DVariable publicVar = new DVariable("publicVar", Visibility.isPublic, false, false, "String");
        DVariable protectedVar = new DVariable("protectedVar", Visibility.isProtected, false, false, "String");

        DFunction privateFunc = new DFunction("privateFunc", Visibility.isPrivate, false, false);
        DFunction publicFunc = new DFunction("publicFunc", Visibility.isPublic, false, false);
        DFunction protectedFunc = new DFunction("protectedFUnc", Visibility.isProtected, false, false);

        Params maxLength = new Params("-r -m 2",null);

        classObject.addVariable(privateVar);
        classObject.addVariable(publicVar);
        classObject.addVariable(protectedVar);

        abstractClassObject.addFunction(privateFunc);
        abstractClassObject.addFunction(publicFunc);
        abstractClassObject.addFunction(protectedFunc);

        String expectedOutput = "@startuml\n" +
                "interface " + interfaceObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "abstract class " + abstractClassObject.getName() + " {\n" +
                "--\n" +
                "- " + privateFunc.getName() + "\n" +
                "+ " + publicFunc.getName() + "\n" +
                "... 1 more functions\n" +
                "}\n" +
                "class " + classObject.getName() + " {\n" +
                "- " + privateVar.getName() + "\n" +
                "+ " + publicVar.getName() + "\n" +
                "... 1 more variables\n" +
                "--\n" +
                "}\n" +
                "@enduml";

        try {
            String output = parseClassAndGetOutput(maxLength);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testFunctionAndVariablesZeroMaxLength() {
        DVariable privateVar = new DVariable("privateVar", Visibility.isPrivate, false, false, "String");
        DVariable publicVar = new DVariable("publicVar", Visibility.isPublic, false, false, "String");
        DVariable protectedVar = new DVariable("protectedVar", Visibility.isProtected, false, false, "String");

        DFunction privateFunc = new DFunction("privateFunc", Visibility.isPrivate, false, false);
        DFunction publicFunc = new DFunction("publicFunc", Visibility.isPublic, false, false);
        DFunction protectedFunc = new DFunction("protectedFUnc", Visibility.isProtected, false, false);

        Params maxLength = new Params("-r -m 0",null);

        classObject.addVariable(privateVar);
        classObject.addVariable(publicVar);
        classObject.addVariable(protectedVar);

        abstractClassObject.addFunction(privateFunc);
        abstractClassObject.addFunction(publicFunc);
        abstractClassObject.addFunction(protectedFunc);

        String expectedOutput = "@startuml\n" +
                "interface " + interfaceObject.getName() + " {\n" +
                "}\n" +
                "abstract class " + abstractClassObject.getName() + " {\n" +
                "}\n" +
                "class " + classObject.getName() + " {\n" +
                "}\n" +
                "@enduml";

        try {
            String output = parseClassAndGetOutput(maxLength);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testPackages() {
        abstractClassObject.setPackageName("Package 1");
        classObject.setPackageName("Package 2");

        String expectedOutput = "@startuml\n" +
                "interface " + interfaceObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "package " + abstractClassObject.getPackageName() + "{\n" +
                "abstract class " + abstractClassObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "}\n" +
                "package " + classObject.getPackageName() + "{\n" +
                "class " + classObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "}\n" +
                "@enduml";

        try {
            String output = parseClassAndGetOutput(defaultParams);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testStraightLinesOn() {
        String expectedOutput = "@startuml\n" +
                "skinparam linetype polyline\n" +
                "interface " + interfaceObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "abstract class " + abstractClassObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "class " + classObject.getName() + " {\n" +
                "--\n" +
                "}\n" +
                "@enduml";

        Params emptyParam = new Params("",null);

        try {
            String output = parseClassAndGetOutput(emptyParam);
            assertEquals(expectedOutput, output);
        } catch (Exception e) {
            fail();
        }
    }

}
