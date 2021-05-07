package src.ui;
import src.ast.DClass;
import src.ast.Params;
import src.ast.Parser;
import src.ast.PlantUMLGenerator;
import src.ast.PlantUMLHandler;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        Params params = parseArgs(args);
        Parser parser = new Parser(params);
        List<DClass> classes = parser.GetClassHierarchy();
        if (classes.size() > 0) {
            String outputFileName = "output";
            PlantUMLGenerator generator = new PlantUMLGenerator(outputFileName, params);
            generator.generateDiagram(classes);
        } else {
            if (!params.isSilent()) System.err.println("No available .java classes in provided paths");
            exit(4);
        }
    }

    public static Params parseArgs(String[] args) {
        if (args.length == 0 || (args.length == 1 && args[0].equals(""))) return doUserDialog();
        Params out = new Params();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-h") || arg.equals("--h") || arg.equals("-help") || arg.equals("--help")) {
                printHelp();
                exit(0);
            } else if (arg.equals("-i") || arg.equals("-input")) {
                if (i == args.length - 1) {
                    System.err.println("Invalid parameters: " + arg + " parameter that was not followed by a relative path");
                    exit(2);
                }
                String path = args[i + 1];
                if (path.contains("\"")) {
                    while (path.contains("\"") && (i + 1) < args.length - 1) {
                        i++;
                        path = path.concat(" " + args[i + 1]);
                    }
                    path = path.replaceAll("(\")", "");
                    out.addPath(path);
                    i++;
                } else if (path.contains(",")){
                    while (path.contains(",") && (i+1)<args.length -1) {
                        List<String> paths = Arrays.asList(path.split("\\s*,\\s*"));
                        for (int j = 0; j < paths.size(); j++) {
                            if (!paths.get(j).equals("")){
                                out.addPath(paths.get(j));
                             }
                        }
                        i++;
                        path = args[i+1];
                    }
                    if ((i+1) >= args.length - 1) {
                        List<String> paths = Arrays.asList(path.split("\\s*,\\s*"));
                        for (int j = 0; j < paths.size(); j++) {
                            if (!paths.get(j).equals("")){
                                out.addPath(paths.get(j));
                            }
                        }
                        i++;
                    } else {
                        out.addPath(args[i+1]);
                        i++;
                    }
                }
                else{
                    out.addPath(args[i+1]);
                    i++;
                }
            } else if (arg.equals("-m") || arg.equals("-max_class_length")) {
                if (((i+1)<args.length) && (args[i + 1].matches("[0-9]+"))){
                    out.setMaxLengthNum(Integer.parseInt(args[i + 1]));
                    i++; //Jump past next argument since it was parsed here
                }
                out.setMaxLength(true);
            } else if (arg.equals("-c") || arg.equals("-coupling")) {
                out.setDisplayCoupling(true);
            } else if (arg.equals("-r") || arg.equals("-round_lines")) {
                out.setStraightLines(false);
            } else if (arg.equals("-s") || arg.equals("-silent")) {
                out.setSilent(true);
            } else {

                System.out.println("Could not interpret argument: " + arg + " of " + Arrays.toString(args));
                exit(1);
            }
        }
        return out;
    }

    private static void printHelp() {
        System.out.println("To change features of UML diagram (Press enter to do nothing): \n");
        System.out.println("- -r or -round_lines                        Turn on round lines.");
        System.out.println("- -c or -coupling                           Coupling analysis is visible.");
        System.out.println("- -m [num] or -max_class_length [num]       Specify max number of lines to display in each class.");
        System.out.println("- -m or -max_class_length                   Max number of lines is 5.");
        System.out.println("- -s or -silent                             program will not print to stdout or stderr");
    }

    private static Params doUserDialog() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        System.out.println("Please enter your file paths (separate paths with commas): ");
        String path = scanner.nextLine();
        List<String> paths = Arrays.asList(path.split("\\s*,\\s*"));
        System.out.println("To change features of UML diagram (Press enter to do nothing): \n");
        System.out.println("- -r or -round_lines                        Turn on round lines.");
        System.out.println("- -c or -coupling                           Coupling analysis is visible.");
        System.out.println("- -m [num] or -max_class_length [num]       Specify max number of lines to display in each class.");
        System.out.println("- -m or -max_class_length                   Max number of lines is 5.");
        System.out.println("- -s or -silent                             (add description)");
        System.out.println("\nEnter any features you wish to add to the diagram, and separate commands by spaces: ");
        String flags_str = scanner.nextLine();
        return new Params(flags_str, paths);
    }
}
