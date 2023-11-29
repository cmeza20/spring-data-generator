package com.cmeza.sdgenerator.plugin;

import com.cmeza.sdgenerator.support.RepositoryTemplateSupport;
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
@Mojo(name = "repositories")
@Execute(phase = LifecyclePhase.COMPILE)
@SuppressWarnings("unused")
public class SDRepositoryMojo extends CommonsMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        SDLogger.configure(getLog());

        this.validateField(Constants.ENTITY_PACKAGE);
        this.validateField(Constants.REPOSITORY_PACKAGE);
        this.validateField(Constants.MAGANER_PACKAGE);
        this.validateField(Constants.EXTENDS);

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
        resourceLoader.setPostfix(generatorProperties.getRepositoryPostfix());
        resourceLoader.setOverwrite(generatorProperties.isOverwrite());

        String absolutePath = GeneratorUtils.getAbsolutePath(generatorProperties.getRepositoryPackage());
        if (absolutePath == null) {
            SDLogger.addError("Could not define the absolute path of the repositories");
            throw new SDMojoException();
        }

        ScanningConfigurationSupport scanningConfigurationSupport = new ScanningConfigurationSupport(generatorProperties.getEntityPackage(), generatorProperties.isOnlyAnnotations());

        RepositoryTemplateSupport repositoryTemplateSupport = new RepositoryTemplateSupport(resourceLoader, generatorProperties.getAdditionalExtendsList(), generatorProperties.isWithComments(), generatorProperties.isWithJpaSpecificationExecutor());
        repositoryTemplateSupport.initializeCreation(absolutePath, generatorProperties.getRepositoryPackage(), scanningConfigurationSupport.getCandidates(resourceLoader), generatorProperties.getEntityPackage());

    }

}
