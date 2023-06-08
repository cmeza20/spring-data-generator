package com.cmeza.sdgenerator.plugin;

import com.cmeza.sdgenerator.util.Constants;
import com.cmeza.sdgenerator.util.GeneratorUtils;
import com.cmeza.sdgenerator.util.SDLogger;
import com.cmeza.sdgenerator.util.SDMojoException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Created by carlos on 25/04/22.
 */
@Mojo(name = "all")
@Execute(phase = LifecyclePhase.COMPILE)
@SuppressWarnings("unused")
public class SDAllMojo extends CommonsMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        SDLogger.configure(getLog());

        this.validateField(Constants.ENTITY_PACKAGE);
        this.validateField(Constants.REPOSITORY_PACKAGE);
        this.validateField(Constants.EXTENDS);

        try {
            GeneratorUtils.setBaseDir(generateDirectory);
            SDRepositoryMojo sdRepositoryMojo = new SDRepositoryMojo();
            sdRepositoryMojo.executeInternalMojo(project, repositoryPostfix, overwrite, repositoryPackage, entityPackage, onlyAnnotations, additionalExtendsList, withComments);

            SDManagerMojo sdManagerMojo = new SDManagerMojo();
            sdManagerMojo.executeInternalMojo(project, managerPostfix, repositoryPackage, repositoryPostfix, overwrite, managerPackage, entityPackage, onlyAnnotations, lombokAnnotations, withComments);

            SDLogger.printGeneratedTables(true);
        } catch (Exception e) {
            SDLogger.addError(e.getMessage());
            throw new SDMojoException(e.getMessage(), e);
        }
    }

}
