package com.cmeza.sdgenerator.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by carlos on 08/04/17.
 */
public class GeneratorUtils {

    private static final Log logger = LogFactory.getLog(GeneratorUtils.class);

    public static String getAbsolutePath(){
        try {
            return new File(".").getCanonicalPath() + "/src/main/java/";
        } catch (IOException e) {
            logger.error("Failed to get the general path: " + e.getMessage());
        }
        return null;
    }

    public static boolean verifyPackage(String StringPath){
        Path path = Paths.get(StringPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                return true;
            } catch (IOException e) {
                logger.error(String.format("Could not create directory: %s ", StringPath) + e.getMessage());
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
        char c[] = cad.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }
}
