package src.ast;

import java.util.*;

public class Analyzer {
    // Instructions: To get the degrees of each class, call:
    // Analyzer analyzer = new Analyzer(classes);
    // Map<String, Integer> degreesList = analyzer.getDegreesMap();
    private Map<String, Integer> degreesMap = new HashMap<>();
    private int numClasses;
    public Analyzer(List<DClass> classes) {
        numClasses = classes.size();
        for (DClass c: classes) {
            calculateDegrees(c);
        }
    }

    private void calculateDegrees(DClass c) {
        int degrees = 0;
        degrees += calculateExtends(c);
        degrees += calculateUseClass(c);
        degrees += calculateImplementations(c);
        // scale from 0-510, 0 being no coupling, 510 being most coupling
        double scaledDegrees = (double) degrees / (double) numClasses * 510;
        degreesMap.put(c.getName(), scaledDegrees > 510 ? 510 : (int) scaledDegrees);
    }


    private int calculateExtends(DClass c) {
        if (c.getExtendsClass() == null) return 0;
        if (c.getExtendsClass().getPackageName() != null
                && c.getExtendsClass().getPackageName().equals(c.getPackageName()))
            return 0;
        return 1;
    }

    private int calculateUseClass(DClass c) {
        if (c.getUseClass() == null) return 0;
        String packageNameC = c.getPackageName();
        int num = 0;
        for (DClass useClass: c.getUseClass()) {
            if (packageNameC != null && useClass.getPackageName() != null &&
                     useClass.getPackageName().equals(packageNameC)) continue;
            else num++;
        }
        return num;
    }

    private int calculateImplementations(DClass c) {
        if (c.getImplementsClass() == null) return 0;
        if (c.getPackageName() != null && c.getImplementsClass().getPackageName() != null
                && c.getImplementsClass().getPackageName().equals(c.getPackageName())) return 0;
        return 1;
    }


    public Map<String, Integer> getDegreesMap() {
        return degreesMap;
    }
}
