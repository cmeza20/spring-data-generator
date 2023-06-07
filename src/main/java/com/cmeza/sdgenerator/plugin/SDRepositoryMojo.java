package com.cmeza.sdgenerator.plugin;

import com.cmeza.sdgenerator.support.RepositoryTemplateSupport;
import com.cmeza.sdgenerator.support.ScanningConfigurationSupport;
import com.cmeza.sdgenerator.util.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.util.Set;

/**
 * Created by carlos on 22/04/17.
 */
@Mojo(name = "repositories")
@Execute(phase = LifecyclePhase.COMPILE)
@SuppressWarnings("unused")
public class SDRepositoryMojo extends CommonsMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        SDLogger.configure(getLog());

        this.validateField(Constants.ENTITY_PACKAGE);
        this.validateField(Constants.REPOSITORY_PACKAGE);
        this.validateField(Constants.MAGANER_PACKAGE);
        this.validateField(Constants.EXTENDS);

        try {
            GeneratorUtils.setBaseDir(project.getBasedir().getCanonicalPath());
            this.executeInternalMojo(project, repositoryPostfix, overwrite, repositoryPackage, entityPackage, onlyAnnotations, additionalExtendsList, withComments);
            SDLogger.printGeneratedTables(true);
        } catch (IOException e) {
            SDLogger.addError("Could not define the absolute path!");
            throw new SDMojoException();
        } catch (Exception e) {
            SDLogger.addError(e.getMessage());
            throw new SDMojoException(e.getMessage(), e);
        }
    }

    public void executeInternalMojo(MavenProject project, String repositoryPostfix, boolean overwrite, String repositoryPackage, String[] entityPackage, boolean onlyAnnotations, Set<String> additionalExtendsList, boolean withComments) throws MojoExecutionException, MojoFailureException {
        CustomResourceLoader resourceLoader = new CustomResourceLoader(project);
        resourceLoader.setPostfix(repositoryPostfix);
        resourceLoader.setOverwrite(overwrite);

        String absolutePath = GeneratorUtils.getAbsolutePath(repositoryPackage);
        if (absolutePath == null){
            SDLogger.addError("Could not define the absolute path of the repositories");
            throw new SDMojoException();
        }

        ScanningConfigurationSupport scanningConfigurationSupport = new ScanningConfigurationSupport(entityPackage, onlyAnnotations);

        RepositoryTemplateSupport repositoryTemplateSupport = new RepositoryTemplateSupport(resourceLoader, additionalExtendsList, withComments);
        repositoryTemplateSupport.initializeCreation(absolutePath, repositoryPackage, scanningConfigurationSupport.getCandidates(resourceLoader), entityPackage);

    }

}
