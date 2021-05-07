package src.ast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    static List<File> findAndLoadFiles(List<String> lop, Params params) {
        List<File> out = new ArrayList<>();
        for (String path : lop) {
            out.addAll(findAndAddClasses(path, params));
        }
        return out;
    }

    private static List<File> findAndAddClasses(String path, Params params) {
        File file = new File(path);
        List<File> out = new ArrayList<>();
        if (file.exists()) {
            if (file.isDirectory()) {
                out.addAll(getChildrenFiles(file, params));
            } else {
                if (checkValidity(file)) out.add(file);
            }
        } else {
            if (!params.isSilent()) System.err.println("Error: " + path + " does not exist. Skipping this path.");
        }
        return out;
    }

    // processes the children of directory files
    private static List<File> getChildrenFiles(File parent, Params params) {
        List<File> out = new ArrayList<>();
        File[] children = parent.listFiles();
        if (children != null) {
            for (File child : children) {
                out.addAll(findAndAddClasses(child.toString(), params));
            }
        }
        return out;
    }

    // checks that the files are .java files
    private static boolean checkValidity(File file) {
        String extension = "";
        int i = file.toString().lastIndexOf('.');
        if (i > 0) {
            extension = file.toString().substring(i + 1);
        }
        return extension.equals("java");
    }
}
