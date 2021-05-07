package src.ast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

/**
 * Object for creating class diagrams from PlantUML .txt files
 */
public class PlantUMLHandler {

    private final String outputFilePath;
    private final String outputFileName;

    public PlantUMLHandler(String outputFileName) {
        this.outputFilePath = "*/output";
        this.outputFileName = outputFileName;
    }

    public void generateClassDiagram() {
            String generateDiagramCmd = "java -jar plantuml.jar " + this.outputFileName;
            try {
                Process process = Runtime.getRuntime().exec(generateDiagramCmd);
                printResults(process);
            } catch (IOException e) {
                System.err.println("Error generating UML class diagram");
                e.printStackTrace();
                exit(1);
            }
    }

    public void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

}
