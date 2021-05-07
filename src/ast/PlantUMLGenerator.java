package src.ast;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class PlantUMLGenerator {

    private PrintWriter writer;
    private String outputDirPath;
    private String outputFile;
    private StringBuilder outputString;
    private Params params;
    private PlantUMLHandler handler;

    private Analyzer analyzer;
    private Map<String, Integer> degreesMap = new HashMap<>();

    public PlantUMLGenerator(String outFilename, Params params) {
        outputDirPath = System.getProperty("user.dir") + "/output";
        outputFile = outFilename + ".puml";
        handler = new PlantUMLHandler(outFilename);
        this.params = params;
        createOutputDir();
        try {
            File output = createOutputFile();
            writer = new PrintWriter(output);
        } catch (IOException e) {
            System.err.println("Error creating output file");
            e.printStackTrace();
            exit(1);
        }
        outputString = new StringBuilder();
        outputString.append("@startuml");
        appendNewline();
        if (this.params.isStraightLines()) {
            outputString.append("skinparam linetype polyline");
            appendNewline();
        }
    }

    /**
     * Creates a directory for the output file if it doesn't exist
     */
    private void createOutputDir() {
        File directory = new File(outputDirPath);
        if (!directory.exists()) {
            boolean success = directory.mkdir();
        }
    }

    /**
     * Creates a output file in the output directory
     * @return output file
     * @throws IOException: Error occurred when creating file
     */
    private File createOutputFile() throws IOException {
        File outputFile = new File(outputDirPath + "/" + this.outputFile);
        boolean success = outputFile.createNewFile();
        return outputFile;
    }

    /**
     * Appends newline to outputString buffer
     */
    private void appendNewline() {
        outputString.append("\n");
    }

    /**
     * Returns outputString in string format
     */
    public String getOutputString() {
        return outputString.toString();
    }

    /**
     * Close the file and generate the PNG
     */
    public void closeFile() {
        outputString.append("@enduml");
        writer.println(this.outputString.toString());
        writer.close();
    }

    /**
     * Generated a hex colour code based on the given int 0 < code < 510
     * * 0 -> Green
     * * 510 -> red
     * @return Hexcode value of a colour ranging from green -> red
     */
    private String generateColourCode(int code) {
        String redComponent;
        String greenComponent;
        String blueComponent = "00";

        // colour scaling, this gives us nice shades of green -> red
        if (code > 255) {
            int processedCode = code - 255;
            redComponent = "FF";
            greenComponent = Integer.toHexString(255 - processedCode);

        } else {
            redComponent = Integer.toHexString(code);
            greenComponent = "FF";
        }

        // append leading 0 if components are length 1
        if (redComponent.length() < 2) {
            redComponent = "0" + redComponent;
        }
        if (greenComponent.length() < 2) {
            greenComponent = "0" + greenComponent;
        }
        if (blueComponent.length() < 2) {
            blueComponent = "0" + blueComponent;
        }
        return redComponent + greenComponent + blueComponent;
    }

    public void generateDiagram(List<DClass> classes) {
        parseClasses(classes);
        closeFile();
        handler.generateClassDiagram();
    }

    /**
     * parse classes without generating diagram. For testing purposes only
     */
    public void testParseClasses(List<DClass> classes) {
        parseClasses(classes);
        closeFile();
    }

    private void parseClasses(List<DClass> classes) {

        analyzer = new Analyzer(classes);
        degreesMap = analyzer.getDegreesMap();

        // create each class
        for (DClass c: classes) {

            String colourCode = generateColourCode(degreesMap.get(c.getName()));

            int maxLines = 0;
            if (params.isMaxLength())
                maxLines = params.getMaxLengthNum();

            if (c.getPackageName() != null) {
                outputString.append("package ").append(c.getPackageName()).append("{");
                appendNewline();
            }
            if (c.isAbstract()) {
                if (params.isDisplayCoupling())
                    outputString.append("abstract class ").append(c.getName()).append(" #").append(colourCode).append(" {");
                else
                    outputString.append("abstract class ").append(c.getName()).append(" {");
                appendNewline();
            }
            else if (c.isInterface()) {
                if (params.isDisplayCoupling())
                    outputString.append("interface ").append(c.getName()).append(" #").append(colourCode).append(" {");
                else
                    outputString.append("interface ").append(c.getName()).append(" {");
                appendNewline();
            }
            else if (c.isEnum()) {
                if (params.isDisplayCoupling())
                    outputString.append("enum ").append(c.getName()).append(" #").append(colourCode).append(" {");
                else
                    outputString.append("enum ").append(c.getName()).append(" {");
                appendNewline();
            }
            else {
                if (params.isDisplayCoupling())
                    outputString.append("class ").append(c.getName()).append(" #").append(colourCode).append(" {");
                else
                    outputString.append("class ").append(c.getName()).append(" {");
                appendNewline();
            }

            // Variables
            if (c.getVariables() != null) {
                for (DVariable v : c.getVariables()) {
                    if (params.isMaxLength()) {
                        if (maxLines < 1) {
                            int leftoverLength = c.getVariables().size() - params.getMaxLengthNum();
                            if (leftoverLength > 0 && params.getMaxLengthNum() != 0) {
                                outputString.append("... ").append(leftoverLength).append(" more variables\n");
                            }
                            break;
                        }
                        else {
                            maxLines--;
                        }
                    }
                    switch (v.getVisibility()) {
                        case isPublic    -> outputString.append("+ ").append(v.getName()).append("\n");
                        case isPrivate   -> outputString.append("- ").append(v.getName()).append("\n");
                        case isProtected -> outputString.append("# ").append(v.getName()).append("\n");
                    }
                }
            }

            // Horizontal line, do not add this if vars and funcs are empty
            if (params.getMaxLengthNum() != 0) {
                outputString.append("--");
                appendNewline();
            }

            // Functions
            if (c.getFunctions() != null) {
                for (DFunction f : c.getFunctions()) {
                    if (params.isMaxLength()) {
                        if (maxLines < 1) {
                            int leftoverLength = c.getFunctions().size() - params.getMaxLengthNum();
                            if (leftoverLength > 0 && params.getMaxLengthNum() != 0) {
                                outputString.append("... ").append(leftoverLength).append(" more functions\n");
                            }
                            break;
                        }
                        else {
                            maxLines--;
                        }
                    }
                    switch (f.getVisibility()) {
                        case isPublic    -> outputString.append("+ ").append(f.getName()).append("\n");
                        case isPrivate   -> outputString.append("- ").append(f.getName()).append("\n");
                        case isProtected -> outputString.append("# ").append(f.getName()).append("\n");
                    }
                }
            }

            //....
            outputString.append("}");
            appendNewline();
            if (c.getPackageName() != null) {
                outputString.append("}");
                appendNewline();
            }
        }

        // take care of Extends and Implements and Use
        for (DClass c: classes) {
            if (c.getExtendsClass() != null) {
                outputString.append(c.getName()).append(" --|> ").append(c.getExtendsClass().getName()).append("\n");
                appendNewline();
            }

            if (c.getImplementsClass() != null) {
                outputString.append(c.getName()).append(" ..|> ").append(c.getImplementsClass().getName()).append("\n");
                appendNewline();
            }

            // Use
            if (c.getUseClass() != null) {
                for (DClass d : c.getUseClass()) {
                    outputString.append(c.getName()).append(" ..> ").append(d.getName()).append("\n");
                    appendNewline();
                }
            }
        }
    }

}
