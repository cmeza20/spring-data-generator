package com.cmeza.sdgenerator.plugin;

import com.cmeza.sdgenerator.support.ManagerTemplateSupport;
import com.cmeza.sdgenerator.support.ScanningConfigurationSupport;
import com.cmeza.sdgenerator.util.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;

/**
 * Created by carlos on 22/04/17.
 */
@Mojo(name = "managers")
@Execute(phase = LifecyclePhase.COMPILE)
@SuppressWarnings("unused")
public class SDManagerMojo extends CommonsMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        SDLogger.configure(getLog());

        this.validateField(Constants.ENTITY_PACKAGE);
        this.validateField(Constants.MAGANER_PACKAGE);
        this.validateField(Constants.REPOSITORY_PACKAGE);

        try {
            CustomResourceLoader resourceLoader = new CustomResourceLoader(project);
            resourceLoader.setPostfix(managerPostfix);
            resourceLoader.setRepositoryPackage(repositoryPackage);
            resourceLoader.setRepositoryPostfix(repositoryPostfix);
            resourceLoader.setOverwrite(overwrite);

            String absolutePath = GeneratorUtils.getAbsolutePath(managerPackage);
            if (absolutePath == null){
                SDLogger.addError("Could not define the absolute path of the managers");
                throw new SDMojoException();
            }

            ScanningConfigurationSupport scanningConfigurationSupport = new ScanningConfigurationSupport(entityPackage, onlyAnnotations);

            ManagerTemplateSupport managerTemplateSupport = new ManagerTemplateSupport(resourceLoader);
            managerTemplateSupport.initializeCreation(absolutePath, managerPackage, scanningConfigurationSupport.getCandidates(resourceLoader), entityPackage);

            SDLogger.printGeneratedTables(true);

        } catch (Exception e) {
            SDLogger.addError(e.getMessage());
            throw new SDMojoException(e.getMessage(), e);
        }
    }
    
}
