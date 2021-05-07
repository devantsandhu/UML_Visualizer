package src.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import src.ast.Params;
import src.ui.Main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CLITest {

    @Test
    void testNoArgs() {
        Params expected = new Params();
        String input_string = "";
        String[] args = input_string.split(" ");
        System.out.println("this: " + args[0]);

        String input = "\n\n";
        InputStream fakeIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(fakeIn);
        Params actual = Main.parseArgs(args);
        assertEquals(actual.getPaths(), expected.getPaths());
        assertEquals(actual, expected);
    }

    @Test
    void testMax3() {
        Params expected = new Params();
        expected.setMaxLength(true);
        expected.setMaxLengthNum(3);
        String input_string = "-m 3";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testMax3_2() {
        Params expected = new Params();
        expected.setMaxLength(true);
        expected.setMaxLengthNum(3);
        String input_string = "-max_class_length 3";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testCurved() {
        Params expected = new Params();
        expected.setStraightLines(false);
        String input_string = "-r";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testCurved_2() {
        Params expected = new Params();
        expected.setStraightLines(false);
        String input_string = "-round_lines";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testCoupling() {
        Params expected = new Params();
        expected.setDisplayCoupling(true);
        String input_string = "-coupling";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testCoupling_2() {
        Params expected = new Params();
        expected.setDisplayCoupling(true);
        String input_string = "-c";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testSilent() {
        Params expected = new Params();
        expected.setSilent(true);
        String input_string = "-silent";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testSilent_2() {
        Params expected = new Params();
        expected.setSilent(true);
        String input_string = "-s";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testSimpleInput() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src");
        }});
        String input_string = "-i src";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testMultipleInput() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src/ui");
            add("src/test");
            add("src/main");
        }});
        String input_string = "-i src/ui,src/test,src/main";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testInputWithSpaces() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("test_data/A folder with space/src");
        }});
        String input_string = "-i \"test_data/A folder with space/src\"";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void testMultipleCommands() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src");
        }});
        expected.setMaxLength(true);
        expected.setMaxLengthNum(7);
        expected.setDisplayCoupling(true);
        expected.setSilent(true);
        String input_string = "-i src -m 7 -c -s";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void MultipleIs() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src/ui");
            add("src/test");
            add("src/main");
        }});
        String input_string = "-i src/ui -i src/test -i src/main";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void CommaSpace() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src/ui");
            add("src/test");
            add("src/main");
        }});
        String input_string = "-i src/ui, src/test, src/main";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void CommasAndFlags() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src/ui");
            add("src/test");
            add("src/main");
        }});
        expected.setDisplayCoupling(true);
        expected.setMaxLength(true);
        String input_string = "-i src/ui, src/test, src/main -m -c";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void CommasAndFlagsWithNum() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src/ui");
            add("src/test");
            add("src/main");
        }});
        expected.setDisplayCoupling(true);
        expected.setMaxLength(true);
        expected.setMaxLengthNum(20);
        String input_string = "-i src/ui, src/test, src/main -m 20 -c";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void MultipleIsAndCommas() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src/ui");
            add("src/test");
            add("src/main");
        }});
        expected.setDisplayCoupling(true);
        expected.setMaxLength(true);
        expected.setMaxLengthNum(20);
        String input_string = "-i src/ui, src/test -i src/main -m 20 -c";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void SpaceAndNoSpaceBetweenPaths() {
        Params expected = new Params();
        expected.setPaths(new ArrayList<String>() {{
            add("src/ui");
            add("src/test");
            add("src/main");
        }});
        expected.setDisplayCoupling(true);
        expected.setMaxLength(true);
        expected.setMaxLengthNum(20);
        String input_string = "-i src/ui,src/test, src/main -m 20 -c";
        String[] args = input_string.split(" ");
        Params actual = Main.parseArgs(args);
        assertEquals(actual, expected);
    }

    @Test
    void IllegalCommandsMixedIn() {
        SecurityManager initialSecurityManager = System.getSecurityManager();
        try {
            System.setSecurityManager(new NoExitSecurityManager());
            Params expected = new Params();
            String input_string = "-i src asdfasdf -m";
            String[] args = input_string.split(" ");
            Params actual = Main.parseArgs(args);
            assertEquals(actual, expected);
            fail("should not reach here");
        } catch (ExitException e) {
            assertEquals(e.getStatus(), 1);
        } finally {
            System.setSecurityManager(initialSecurityManager);
        }
    }

    @Test
    void IllegalCommand() throws SecurityException{
        SecurityManager initialSecurityManager = System.getSecurityManager();
        try {
            System.setSecurityManager(new NoExitSecurityManager());
            Params expected = new Params();
            String input_string = "-asdfasdf";
            String[] args = input_string.split(" ");
            Params actual = Main.parseArgs(args);
            assertEquals(actual, expected);
            fail("should not reach here");
        } catch (ExitException e) {
            assertEquals(e.getStatus(), 1);
        } finally {
            System.setSecurityManager(initialSecurityManager);
        }
    }
}