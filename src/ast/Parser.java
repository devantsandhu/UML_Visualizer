package src.ast;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
    private List<DClass> classes;
    static final String STRING_REPLACEMENT = "*~^*~*^~*";
    private Params params;

    /*
    Parser recursively searches for .java files in all directories listed by lop
    and loads them into.
     */
    public Parser(Params params) {
        this.params = params;
        List<File> files = FileParser.findAndLoadFiles(params.getPaths(), params);
        classes = parseFiles(files); //First pass: Parse class names but not bodies
        parseClassBodies(); //Second pass: Now that all class stubs exist, parse their bodies
    }

    public Parser(List<String> paths) {
        this.params = new Params();
        List<File> files = FileParser.findAndLoadFiles(paths, params);
        classes = parseFiles(files); //First pass: Parse class names but not bodies
        parseClassBodies(); //Second pass: Now that all class stubs exist, parse their bodies
    }

    public List<DClass> GetClassHierarchy() {
        return classes;
    }

    /*
    Parses all java files in files to create a list of DClass
    Does not parse class bodies, but rather adds them as list of string
     */
    private List<DClass> parseFiles(List<File> files) {
        // parses each line of the file and gets rid of comments, empty lines, etc.
        List<DClass> out = new ArrayList<>();
        for (File file : files) {
            LinkedList<String> body = getBody(file);
            String name = file.getName().replace(".java", "");
            DClass dclass = new DClass(name, body);
            out.add(dclass);
        }
        return out;
    }

    // removes comments & strings from body of each class then parses the body and populates class
    private void parseClassBodies() {
        for (DClass dclass : classes) {
            cleanBody(dclass.getBody());
            parseClassBody(dclass);
        }
    }

    private LinkedList<String> getBody(File file) {
        LinkedList<String> body = new LinkedList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                line = line.trim(); //Remove leading and trailing whitespace
                if (!line.equals("")) {
                    body.add(line);
                }
            }
        } catch (IOException e) {
            if (!params.isSilent()) {
                System.err.println("Error reading class body");
                e.printStackTrace();
            }
        }
        return body;
    }

    //Removes and replaces parts of the body that are not relevant to the analysis
    private void cleanBody(LinkedList<String> body) {
        removeWhiteSpaces(body);
        removeComments(body);
        replaceStrings(body);
        removeAtKeyWords(body); //E.g. @Test or @Override
        separateMultipleBrackets(body, "{");
        separateMultipleBrackets(body, "}");
        combineLines(body);
        removeEmptyLines(body);
    }

    // merges lines that are split
    private void combineLines(LinkedList<String> body) {
        String newLine = "";
        boolean isSplit = false;
        for (int i = 0; i < body.size(); i++) {
            String line = body.get(i);
            if (!line.contains(";") && !line.contains("{") && !line.contains("}") && !line.equals("")) {
                isSplit = true;
                newLine = newLine.concat(" " + line);
                body.set(i, "");
            } else {
                if (isSplit) {
                    newLine = newLine.concat(" " + line);
                    body.set(i, newLine.strip());
                    isSplit = false;
                    newLine = "";
                } else {
                    body.set(i, line);
                }
            }
        }
    }

    private void removeAtKeyWords(LinkedList<String> body) {
        //Pattern atPattern = Pattern.compile("[^a-zA-Z0-9]?@[a-zA-Z0-9]*"); //Regex to find a class declaration
        for (int i = 0; i < body.size(); i++) {
            String line = body.get(i);
            if (line.contains("@")) {
                line = "";
                body.set(i, line);
            }
        }
    }

    private void removeEmptyLines(LinkedList<String> body) {
        for (int i = 0; i < body.size(); i++) {
            String line = body.get(i);
            if (line.equals("")) {
                body.remove(i);
                i--;
            }
        }
    }

    private void removeWhiteSpaces(LinkedList<String> body) {
        for (int i = 0; i < body.size(); i++) {
            body.set(i, body.get(i).strip());
        }
    }

    private void removeComments(LinkedList<String> body) {
        boolean isComment = false;
        for (int i = 0; i < body.size(); i++) {
            String line = body.get(i);

            //If line starts as comment and contains no end-comment remove that line and run next iteration of loop
            // -> remove the entire line and keep isComment is true for next line
            if (isComment && !line.contains("*/")) {
                body.remove(i);
                i--;//Move index one to left since element was removed
                continue;
                //If line stats as comment and contains and end comment remove that comment and continue checking string for other comments
                // -> remove that comment and set isComment is false
            } else if (isComment && line.contains("*/")) {
                line = line.substring(line.indexOf("*/") + 2);
                isComment = false;
            }

            //Check the line for any comments starting in that line
            int regularIndex = line.indexOf("//");
            int starIndex = line.indexOf("/*");
            while (regularIndex != -1 || starIndex != -1) {
                //Case when line contains regular comment -> remove the rest of the line
                if (starIndex == -1 || (regularIndex != -1 && regularIndex < starIndex)) {
                    line = line.replaceAll("(//.*)", "");
                    break; //Exit while since there are no more comments in line
                } else if (regularIndex == -1 || starIndex < regularIndex) {
                    //Case when line contains an end comment -> remove the star comment and do another iteration of loop
                    if (line.contains("*/")) {
                        String part1 = line.substring(0, starIndex);
                        String part2 = line.substring(line.indexOf("*/") + 2);
                        line = part1.concat(part2);
                    } else {//Case when line contains start of comment but not an end comment
                        // -> remove rest of line and set isComment to true and exit loop
                        line = line.substring(0, starIndex);
                        isComment = true;
                        break;
                    }
                }
                regularIndex = line.indexOf("//");
                starIndex = line.indexOf("/*");
            }
            line = line.strip();
            body.set(i, line);
        }
    }

    // if there are multiple brackets on same line, it will separate them into different lines
    // takes in bracket as string to tackle both the open and closing bracket use cases
    private void separateMultipleBrackets(LinkedList<String> body, String bracket) {
        for (int i = 0; i < body.size(); i++) {
            String line = body.get(i);
            if (line.indexOf(bracket) != line.lastIndexOf(bracket)) {
                List<String> lines = separateLinesByCurlyBrackets(line, bracket);
                for (int j = 0; j < lines.size(); j++) {
                    if (i < body.size()) {
                        body.remove(i);
                    }
                    body.add(i, lines.get(j));
                    i++;
                }
            }
        }
    }

    private List<String> separateLinesByCurlyBrackets(String line, String bracket) {
        List<String> ret = new ArrayList<>();
        int index = line.indexOf(bracket);
        String oldLine = line.substring(0, index + 1);
        String newLine = line.substring(index + 1);
        if (newLine.contains(bracket)) {
            ret.add(oldLine);
            ret.addAll(separateLinesByCurlyBrackets(newLine, bracket));
        } else {
            ret.add(line);
        }
        return ret;
    }

    private void replaceStrings(LinkedList<String> body) {
        for (int i = 0; i < body.size(); i++) {
            String str = body.get(i);
            // if line contains string values, replaces string values with filler words
            if (str.matches("(.*\".*\".*)")) {
                str = str.replaceAll("\"(.*?)\"", STRING_REPLACEMENT);
            }
            str = str.strip();
            body.set(i, str);
        }
    }

    private void parseClassBody(DClass dClass) {
        Pattern classPattern = Pattern.compile("[^a-zA-Z0-9]?class[^a-zA-Z0-9]"); //Regex to find a class declaration
        Pattern interfacePattern = Pattern.compile("[^a-zA-Z0-9]?interface[^a-zA-Z0-9]"); //Regex to find an interface declaration
        Pattern enumPattern = Pattern.compile("[^a-zA-Z0-9]?enum[^a-zA-Z0-9]"); //Regex to find an enum declaration
        Pattern functionPattern = Pattern.compile("[^a-zA-Z0-9][a-zA-Z0-9]*\\("); //Regex to find a function declaration

        DFunction currFunction = null;
        int nested = 0; //Depth of current statement: 0 outside of class, 1 in class, 2+ in function
        for (String line : dClass.getBody()) {
            if (nested == 0) {
                if (line.startsWith("package")) parsePackage(line, dClass);// gets package name
                else if (line.startsWith("import")) parseImport(line, dClass);// gets import
                else if (classPattern.matcher(line).find()) {
                    parseClass(line, dClass);
                } else if (interfacePattern.matcher(line).find()) {
                    parseInterface(line, dClass);
                } else if (enumPattern.matcher(line).find()) {
                    parseEnum(line, dClass);
                } else {
                    if (!params.isSilent()) System.err.println("Unhandled 0: " + line);
                }
            } else if (nested == 1) {
                if (functionPattern.matcher(line).find() && !line.contains("=")) {
                    currFunction = parseFunctionDeclaration(line, dClass);
                    if (currFunction != null) {
                        dClass.addFunction(currFunction);
                    }

                } else if (line.equals("{") || line.equals("}")) {
                    //Do nothing. this case is handled at the bottom
                } else {
                    if (dClass.isEnum() && !line.contains("{")) {
                        parseEnumType(line, dClass);
                    } else {
                        parseClassVariableDeclaration(line, dClass);
                    }
                }
            } else if (nested >= 2) {
                if (line.equals("{") || line.equals("}")) {
                    //Do nothing. this case is handled at the bottom
                } else {
                    parseFunctionBody(line, dClass);
                }
            } else {
                if (!params.isSilent()) System.err.println("Error parsing class: " + dClass.getName() + "!");
                return;
            }
            //Update nested for before handling next line
            if (line.contains("{") || line.contains("}")) nested += handleCurlyBraces(line);
        }
    }

    private void parseEnumType(String line, DClass dClass) {
        String[] subs = line.split("\\s+");
        dClass.addVariable(new DVariable(subs[0], Visibility.fieldNotNeeded, false, false, dClass.getName()));
    }

    private void parseClass(String line, DClass dClass) {
        parseClassHelper(line, dClass);
    }

    private void parseEnum(String line, DClass dClass) {
        dClass.setEnum(true);
        parseClassHelper(line, dClass);
    }

    private void parseInterface(String line, DClass dClass) {
        dClass.setInterface(true);
        parseClassHelper(line, dClass);
    }

    private void parseClassHelper(String line, DClass dClass) {
        dClass.setVisibility(parseVisibility(line));
        dClass.setAbstract(parseAbstract(line));
        dClass.setImplementsClass(parseImplements(line, dClass));
        dClass.setExtendsClass(parseExtends(line, dClass));
    }

    private void parseClassVariableDeclaration(String line, DClass dClass) {
        try {
            Visibility visibility = parseVisibility(line);
            boolean isStatic = parseStatic(line);
            boolean isFinal = parseFinal(line);
            String str = line;
            if (str.contains("=")) {
                str = str.replaceAll("=.*", "");
            }
            str = str.replaceAll(";", "");
            if (visibility != Visibility.isDefault) {
                str = str.replace(visibility.getKeyword(), "");
            }
            if (isStatic) {
                str = str.replace("static", "");
            }
            if (isFinal) {
                str = str.replace("final", "");
            }
            String typeRecovery = str;
            str = str.replaceAll("<[^>]*>", "");
            str = str.strip();
            String[] subs = str.split("\\s+");
            String type = subs[0];
            if (typeRecovery.contains(subs[0] + "<")) {
                type = recoverType(typeRecovery);
            }
            dClass.addVariable(new DVariable(subs[1], visibility, isStatic, isFinal, type));
            DClass dtype = findClass(subs[0]);
            if (dtype != null && !dClass.doesUseClassExistAlready(dtype)) {
                dClass.addUseClass(dtype);
            }
            parseForUseClasses(line, dClass);
        } catch (Exception e) {
            if(!params.isSilent()){
                System.err.println("Error parsing line: " + line);
            }
        }
    }

    private String recoverType(String typeRecovery) {
        String[] subs = typeRecovery.split("\\s+");
        String exclude = subs[subs.length - 1];
        String type = typeRecovery.replace(exclude, "");
        return type.strip();
    }

    private DFunction parseFunctionDeclaration(String line, DClass dClass) {
        String name = parseFunctionName(line);
        Visibility visibility = parseVisibility(line);
        boolean isStatic = parseStatic(line);
        boolean isAbstract = parseAbstract(line);
        parseForUseClasses(line, dClass);
        if (visibility != Visibility.isPrivate && !name.equals(dClass.getName())) { //Only add public functions
            return new DFunction(name, visibility, isStatic, isAbstract);
        }
        return null;
    }

    private String parseFunctionName(String line) {
        String[] subs = line.split("\\s+");
        for (String sub : subs) {
            if (sub.contains("(")) {
                String[] sublets = sub.split("\\(");
                return sublets[0];
            }
        }
        if (!params.isSilent()) System.err.println("Parser Error: Could not find function name: " + line);
        return "ERRORBADFUNCTIONNAME";
    }

    private void parseFunctionBody(String line, DClass dClass) {
        parseForUseClasses(line, dClass);
    }

    private void parseForUseClasses(String line, DClass dClass) {
        String[] subs = line.replaceAll("[^a-zA-Z0-9]", " ").split("\\s+");
        for (String sub : subs) {
            DClass useClass = findClass(sub);
            if (useClass != null && !(useClass == dClass) && !dClass.doesUseClassExistAlready(useClass))
                dClass.addUseClass(useClass);
        }
    }

    private int handleCurlyBraces(String line) {
        if (line.contains("{") && line.contains("}")) return 0;
        if (line.contains("{")) return 1;
        if (line.contains("}")) return -1;
        return 0;
    }

    //This function is currently empty because we are not reading import statements
    private void parseImport(String line, DClass dClass) {
        //This class is purposefully left empty
    }

    private void parsePackage(String line, DClass dClass) {
        String packageName = line.replace("package", "");
        packageName = packageName.replace(";", "");
        packageName = packageName.trim();
        dClass.setPackageName(packageName);
    }

    private Visibility parseVisibility(String line) {
        String[] subs = line.replaceAll("[^a-zA-Z0-9]", " ").split("\\s+");
        for (String sub : subs) {
            if (sub.equals("public")) return Visibility.isPublic;
            if (sub.equals("protected")) return Visibility.isProtected;
            if (sub.equals("private")) return Visibility.isPrivate;
        }
        return Visibility.isDefault;
    }

    private boolean parseAbstract(String line) {
        Pattern abstractPattern = Pattern.compile("\\babstract\\b");
        return abstractPattern.matcher(line).find();
    }

    private boolean parseFinal(String line) {
        Pattern finalPattern = Pattern.compile("\\bfinal\\b");
        return finalPattern.matcher(line).find();
    }

    private boolean parseStatic(String line) {
        Pattern staticPattern = Pattern.compile("\\bstatic\\b");
        return staticPattern.matcher(line).find();
    }

    private DClass parseExtends(String line, DClass dClass) {
        Pattern extendsPattern = Pattern.compile("\\bextends\\b");
        if (!extendsPattern.matcher(line).find()) {
            return null;//No extends keyword
        }
        String[] subs = line.replaceAll("[^a-zA-Z0-9]", " ").split("\\s+");
        for (int i = 0; i < subs.length; i++) {
            String curr = subs[i];
            if (curr.equals("extends")) {
                return findClass(subs[i + 1]);
            }
        }
        if (!params.isSilent()) System.err.println("Parser Error: Could not find class to extend: " + line);
        return null;
    }

    private DClass findClass(String name) {
        for (DClass dClass : classes) {
            if (dClass.getName().equals(name)) return dClass;
        }
        return null;
    }

    private DClass parseImplements(String line, DClass dClass) {
        Pattern implementsPattern = Pattern.compile("\\bimplements\\b");
        if (!implementsPattern.matcher(line).find()) {
            return null;//No implements keyword
        }
        String[] subs = line.replaceAll("[^a-zA-Z0-9]", " ").split("\\s+");
        for (int i = 0; i < subs.length; i++) {
            String curr = subs[i];
            if (curr.equals("implements")) {
                return findClass(subs[i + 1]);
            }
        }
        if (!params.isSilent()) System.err.println("Parser Error: Could not find class to implement: " + line);
        return null;
    }
}
