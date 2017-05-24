package com.cmeza.sdgenerator.util;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Created by carlos on 23/04/17.
 */
public class SDMojoException extends MojoExecutionException {

    public SDMojoException() {
        super("");
        SDLogger.printGeneratedTables(true);
    }

    public SDMojoException(String message) {
        super(message);
        SDLogger.printGeneratedTables(true);
    }

    public SDMojoException(String message, Throwable cause) {
        super(message, cause);
        SDLogger.printGeneratedTables(true);
    }

}
