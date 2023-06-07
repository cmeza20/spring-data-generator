package com.cmeza.sdgenerator.plugin;

import com.cmeza.sdgenerator.util.Constants;
import com.cmeza.sdgenerator.util.SDLogger;
import com.cmeza.sdgenerator.util.SDMojoException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by carlos on 01/05/17.
 */
public abstract class CommonsMojo extends AbstractMojo {

    @Parameter(name = Constants.ENTITY_PACKAGE)
    protected String[] entityPackage;

    @Parameter(name = Constants.REPOSITORY_PACKAGE)
    protected String repositoryPackage;

    @Parameter(name = Constants.REPOSITORY_POSTFIX, defaultValue = "Repository")
    protected String repositoryPostfix;

    @Parameter(name = Constants.MAGANER_PACKAGE)
    protected String managerPackage;

    @Parameter(name = Constants.MANAGER_POSTFIX, defaultValue = "Manager")
    protected String managerPostfix;

    @Parameter(name = Constants.ONLY_ANNOTATIONS, defaultValue = "false")
    protected Boolean onlyAnnotations;

    @Parameter(name = Constants.OVERWRITE, defaultValue = "false")
    protected Boolean overwrite;

    @Parameter(name = Constants.LOMBOK_ANOTATIONS, defaultValue = "false")
    protected Boolean lombokAnnotations;

    @Parameter(name = Constants.WITH_COMMENTS, defaultValue = "true")
    protected Boolean withComments;

    @Parameter(name = Constants.EXTENDS)
    protected String[] additionalExtends;

    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    protected Set<String> additionalExtendsList = new LinkedHashSet<>();

    public void validateField(String parameter) throws SDMojoException {

        boolean errorFound = Boolean.FALSE;

        switch (parameter) {
            case Constants.ENTITY_PACKAGE:
                if (entityPackage == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.REPOSITORY_PACKAGE:
                if (repositoryPackage == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.REPOSITORY_POSTFIX:
                if (repositoryPostfix == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.MAGANER_PACKAGE:
                if (managerPackage == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.MANAGER_POSTFIX:
                if (managerPostfix == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.EXTENDS:
                if (additionalExtends != null) {
                    this.validateExtends();
                }
                break;
            default:
                SDLogger.addError( String.format("%s configuration parameter not found!", parameter));
                throw new SDMojoException();
        }

        if (errorFound) {
            SDLogger.addError( String.format("%s configuration not found!", parameter));
            throw new SDMojoException();
        }
    }

    private void validateExtends() throws SDMojoException {
        String extendTemporal;
        boolean errorValidate = Boolean.FALSE;
        for (int i = 0; i < additionalExtends.length; i++) {
            extendTemporal = additionalExtends[i];
            SDLogger.addAdditionalExtend(extendTemporal);
            if (extendTemporal.contains(".")){
                additionalExtendsList.add(extendTemporal);
            } else {
                errorValidate = Boolean.TRUE;
                SDLogger.addError( String.format("'%s' is not a valid object!", extendTemporal));
            }
        }

        if (errorValidate) {
            throw new SDMojoException();
        }
    }

}
