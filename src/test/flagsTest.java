package src.test;
import org.junit.jupiter.api.Test;
import src.ast.Params;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class flagsTest {

    @Test
    void testEmptyString() {
        String flag_str = "";
        List<String> paths = new ArrayList<>();
        paths.add("test_data/multiline_comment");
        Params params = new Params(flag_str, paths);
        Params referenceFlag = new Params();

        referenceFlag.setMaxLengthNum(5);
        referenceFlag.setMaxLength(false);
        referenceFlag.setDisplayCoupling(false);
        referenceFlag.setStraightLines(true);
        referenceFlag.addPath("test_data/multiline_comment");
        try {
            assertEquals(referenceFlag.isStraightLines(), params.isStraightLines());
            assertEquals(referenceFlag.isDisplayCoupling(), params.isDisplayCoupling());
            assertEquals(referenceFlag.isMaxLength(), params.isMaxLength());
            assertEquals(referenceFlag.getMaxLengthNum(), params.getMaxLengthNum());
            assertEquals(referenceFlag.getPaths(), params.getPaths());
        } catch (Exception e) {
            fail();
        }
    }

    // invalid input is ignored
    @Test
    void testGibberishString() {
        String flag_str = "dfgsdfg34dfghdfgh";
        List<String> paths = new ArrayList<>();
        paths.add("test_data/multiline_comment");
        Params params = new Params(flag_str, paths);
        Params referenceFlag = new Params();

        referenceFlag.setMaxLengthNum(5);
        referenceFlag.setMaxLength(false);
        referenceFlag.setDisplayCoupling(false);
        referenceFlag.setStraightLines(true);
        referenceFlag.addPath("test_data/multiline_comment");

        try {
            assertEquals(referenceFlag.isStraightLines(), params.isStraightLines());
            assertEquals(referenceFlag.isDisplayCoupling(), params.isDisplayCoupling());
            assertEquals(referenceFlag.isMaxLength(), params.isMaxLength());
            assertEquals(referenceFlag.getMaxLengthNum(), params.getMaxLengthNum());
            assertEquals(referenceFlag.getPaths(), params.getPaths());
        } catch (Exception e) {
            fail();
        }
    }

    // correct input is used
    @Test
    void testCorrectFlagInput() {
        String flag_str = "-round_lines -c -m 20";
        List<String> paths = new ArrayList<>();
        paths.add("test_data/multiline_comment");
        Params params = new Params(flag_str, paths);
        Params referenceFlag = new Params();

        referenceFlag.setMaxLengthNum(20);
        referenceFlag.setMaxLength(true);
        referenceFlag.setDisplayCoupling(true);
        referenceFlag.setStraightLines(false);
        referenceFlag.addPath("test_data/multiline_comment");

        try {
            assertEquals(referenceFlag.isStraightLines(), params.isStraightLines());
            assertEquals(referenceFlag.isDisplayCoupling(), params.isDisplayCoupling());
            assertEquals(referenceFlag.isMaxLength(), params.isMaxLength());
            assertEquals(referenceFlag.getMaxLengthNum(), params.getMaxLengthNum());
            assertEquals(referenceFlag.getPaths(), params.getPaths());
        } catch (Exception e) {
            fail();
        }
    }

    // invalid input inbetween valid input is ignored, while valid input is still used
    @Test
    void testMixedInput() {
        String flag_str = "-r asdfasdfdsfasd -c -m 20";
        List<String> paths = new ArrayList<>();
        paths.add("test_data/multiline_comment");
        Params params = new Params(flag_str, paths);
        Params referenceFlag = new Params();

        referenceFlag.setMaxLengthNum(20);
        referenceFlag.setMaxLength(true);
        referenceFlag.setDisplayCoupling(true);
        referenceFlag.setStraightLines(false);
        referenceFlag.addPath("test_data/multiline_comment");

        try {
            assertEquals(referenceFlag.isStraightLines(), params.isStraightLines());
            assertEquals(referenceFlag.isDisplayCoupling(), params.isDisplayCoupling());
            assertEquals(referenceFlag.isMaxLength(), params.isMaxLength());
            assertEquals(referenceFlag.getMaxLengthNum(), params.getMaxLengthNum());
            assertEquals(referenceFlag.getPaths(), params.getPaths());
        } catch (Exception e) {
            fail();
        }
    }

    // if valid input is separated by invalid input, the whole flag is ignored, and default is used
    @Test
    void testSplitInput() {
        String flag_str = "straight asdfasdfsd lines off asdfasdfdsfasd add  asdfasdf coupling -m";
        List<String> paths = new ArrayList<>();
        paths.add("test_data/multiline_comment");
        Params params = new Params(flag_str, paths);
        Params referenceFlag = new Params();

        referenceFlag.setMaxLengthNum(5);
        referenceFlag.setMaxLength(true);
        referenceFlag.setDisplayCoupling(false);
        referenceFlag.setStraightLines(true);
        referenceFlag.addPath("test_data/multiline_comment");

        try {
            assertEquals(referenceFlag.isStraightLines(), params.isStraightLines());
            assertEquals(referenceFlag.isDisplayCoupling(), params.isDisplayCoupling());
            assertEquals(referenceFlag.isMaxLength(), params.isMaxLength());
            assertEquals(referenceFlag.getMaxLengthNum(), params.getMaxLengthNum());
            assertEquals(referenceFlag.getPaths(), params.getPaths());
        } catch (Exception e) {
            fail();
        }
    }
}
