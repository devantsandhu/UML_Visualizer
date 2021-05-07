package src.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Params {
    private boolean isStraightLines = true;
    private boolean displayCoupling = false;
    private boolean maxLength = false;
    private boolean isSilent = false;
    private int maxLengthNum = 5;

    private List<String> paths;

    public Params(String flag, List<String> paths) {
        parseFlags(flag);
        this.paths = paths;
    }

    public Params() {
        this.displayCoupling = false;
        this.maxLength = false;
        this.isSilent = false;
        this.paths = new ArrayList<String>() {{add("");}};
    }

    private void parseFlags(String flag) {
        if (flag.contains("-r") || flag.contains("-round_lines")) {
            this.isStraightLines = false;
        }
        if (flag.contains("-c") || flag.contains("-coupling")) {
            this.displayCoupling = true;
        }
        if (flag.contains("-s") || flag.contains("-silent")) {
            this.isSilent = true;
        }
        if (flag.contains("-m") || flag.contains("-max_class_length")) {
            this.maxLength = true;
            if (Pattern.compile("(-m [0-9]+)").matcher(flag).find() || Pattern.compile("(-max_class_length [0-9]+)").matcher(flag).find()) {
                this.maxLengthNum = Integer.parseInt(flag.replaceAll("[^0-9]", ""));
            }
        }
        if (!flag.contains("-r") && flag.contains("-round_lines") && !flag.contains("-c") && flag.contains("-coupling")
                && !flag.contains("-m") && !flag.contains("-max_class_length") && !flag.matches("")) {
            System.out.print("Was unable to add flags. UML Class diagram generated with default parameters.");
        }
    }

    public boolean isStraightLines() {
        return isStraightLines;
    }


    public boolean isDisplayCoupling() {
        return displayCoupling;
    }


    public boolean isMaxLength() {
        return maxLength;
    }


    public int getMaxLengthNum() {
        return maxLengthNum;
    }

    public void setStraightLines(boolean straightLines) {
        this.isStraightLines = straightLines;
    }

    public void setDisplayCoupling(boolean displayCoupling) {
        this.displayCoupling = displayCoupling;
    }

    public void setMaxLength(boolean maxLength) {
        this.maxLength = maxLength;
    }

    public void setMaxLengthNum(int maxLengthNum) {
        this.maxLengthNum = maxLengthNum;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public void setSilent(boolean silent) {
        isSilent = silent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Params)) return false;
        Params params = (Params) o;
        boolean equal = getPaths().size() == params.getPaths().size();
        for (int i = 0; i < getPaths().size(); i++) {
            equal = equal && getPaths().get(i).equals(params.getPaths().get(i));
        }
        return equal &&
                isStraightLines() == params.isStraightLines() &&
                isDisplayCoupling() == params.isDisplayCoupling() &&
                isMaxLength() == params.isMaxLength() &&
                isSilent() == params.isSilent() &&
                getMaxLengthNum() == params.getMaxLengthNum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isStraightLines(), isDisplayCoupling(), isMaxLength(), isSilent(), getMaxLengthNum(), getPaths());
    }

    public void addPath(String arg) {
        // if this is the first path in the paths list, replace empty string with first path string
        if (paths.size() == 1 && paths.get(0).equals("")){
            paths.remove(0);
            paths.add(arg);
        } else {
            paths.add(arg);
        }
    }
}
