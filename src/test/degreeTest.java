package src.test;

import org.junit.jupiter.api.Test;
import src.ast.Analyzer;
import src.ast.DClass;
import src.ast.Parser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class degreeTest {

    @Test
    void testLooselyCoupled() {
        String path = "test_data/looselyCoupledFiles";
        List<String> lop = Arrays.asList(path.split("\\s*,\\s*"));
        Parser parser = new Parser(lop);
        List<DClass> classes = parser.GetClassHierarchy();
        Analyzer analyzer = new Analyzer(classes);
        Map<String, Integer> degreesList = analyzer.getDegreesMap();
        // Topic1 and Topic2 should have the same degree of coupling
        assertEquals(degreesList.get("Topic1"), degreesList.get("Topic2"));
        assertEquals(degreesList.get("Subject") / 2, degreesList.get("Topic1"));
    }

    @Test
    void testVeryCoupledClasses() {
        String path = "test_data/tinyVarsUpgradedVisitor/src";
        List<String> lop = Arrays.asList(path.split("\\s*,\\s*"));
        Parser parser = new Parser(lop);
        List<DClass> classes = parser.GetClassHierarchy();
        Analyzer analyzer = new Analyzer(classes);
        Map<String, Integer> degreesList = analyzer.getDegreesMap();
        assertEquals(degreesList.get("Tokenizer"), 0);
        assertEquals(degreesList.get("Set"), 0);
        assertEquals(degreesList.get("tinyVarsParser"), 28);
        assertEquals(degreesList.get("Statement"), 28);
    }

    @Test
    void testSmallCoupledFiles() {
        String path = "test_data/smallCoupledFiles";
        List<String> lop = Arrays.asList(path.split("\\s*,\\s*"));
        Parser parser = new Parser(lop);
        List<DClass> classes = parser.GetClassHierarchy();
        Analyzer analyzer = new Analyzer(classes);
        Map<String, Integer> degreesList = analyzer.getDegreesMap();
        assertEquals(255, degreesList.get("Volume"));
        // no coupling for class Box
        assertEquals(0, degreesList.get("Box"));
    }


}
