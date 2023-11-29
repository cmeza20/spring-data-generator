package com.cmeza.sdgenerator.plugin;

import com.cmeza.sdgenerator.support.ManagerTemplateSupport;
import com.cmeza.sdgenerator.support.ScanningConfigurationSupport;
import com.cmeza.sdgenerator.util.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Created by carlos on 22/04/17.
 */
@Mojo(name = "managers")
@Execute(phase = LifecyclePhase.COMPILE)
@SuppressWarnings("unused")
public class SDManagerMojo extends CommonsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        SDLogger.configure(getLog());

        this.validateField(Constants.ENTITY_PACKAGE);
        this.validateField(Constants.MAGANER_PACKAGE);
        this.validateField(Constants.REPOSITORY_PACKAGE);

        try {
            GeneratorProperties generatorProperties = this.buildProperties();
            this.executeInternalMojo(generatorProperties);

            SDLogger.printGeneratedTables(true);

        } catch (Exception e) {
            SDLogger.addError(e.getMessage());
            throw new SDMojoException(e.getMessage(), e);
        }
    }

    public void executeInternalMojo(GeneratorProperties generatorProperties) throws MojoExecutionException {
        CustomResourceLoader resourceLoader = new CustomResourceLoader(generatorProperties.getProject());
        resourceLoader.setPostfix(generatorProperties.getManagerPostfix());
        resourceLoader.setRepositoryPackage(generatorProperties.getRepositoryPackage());
        resourceLoader.setRepositoryPostfix(generatorProperties.getRepositoryPostfix());
        resourceLoader.setOverwrite(generatorProperties.isOverwrite());

        String absolutePath = GeneratorUtils.getAbsolutePath(generatorProperties.getManagerPackage());
        if (absolutePath == null) {
            SDLogger.addError("Could not define the absolute path of the managers");
            throw new SDMojoException();
        }

        ScanningConfigurationSupport scanningConfigurationSupport = new ScanningConfigurationSupport(generatorProperties.getEntityPackage(), generatorProperties.isOnlyAnnotations());

        ManagerTemplateSupport managerTemplateSupport = new ManagerTemplateSupport(resourceLoader, generatorProperties.isLombokAnnotations(), generatorProperties.isWithComments());
        managerTemplateSupport.initializeCreation(absolutePath, generatorProperties.getManagerPackage(), scanningConfigurationSupport.getCandidates(resourceLoader), generatorProperties.getEntityPackage());
    }

}
