package com.cmeza.sdgenerator.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by carlos on 08/04/17.
 */
public class GeneratorUtils {

    private GeneratorUtils() {
    }

    public static String getAbsolutePath(){
        try {
            return new File(".").getCanonicalPath() + "/src/main/java/";
        } catch (IOException e) {
            return null;
        }
    }

    public static String getAbsolutePath(String strPackage){
        String absolute = getAbsolutePath();
        if (absolute == null) {
            return null;
        }
        return absolute + strPackage.replace(".", "/");
    }

    public static boolean verifyPackage(String stringPath){
        Path path = Paths.get(stringPath);
        if (!path.toFile().exists()) {
            try {
                Files.createDirectories(path);
                return true;
            } catch (IOException e) {
                SDLogger.addError(String.format("Could not create directory: %s ", stringPath) + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public static String getSimpleClassName(String beanClassName) {
        int index = -1;
        for (int i = (beanClassName.length() - 1); i >= 0; i--) {
            if(beanClassName.charAt(i) == '.'){
                index = i;
                break;
            }
        }
        return index == -1 ? null : beanClassName.substring(index + 1);
    }

    public static String decapitalize(String cad) {
        char[] c = cad.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public static File[] getFileList(String dirPath, String prefix) {
        File[] returnFiles = new File(dirPath)
                .listFiles(
                        (dir1, name) -> name.contains(prefix) && name.endsWith(".java")
                );

        if (returnFiles == null) {
            return new File[]{};
        }
        
        return returnFiles;
    }
}
