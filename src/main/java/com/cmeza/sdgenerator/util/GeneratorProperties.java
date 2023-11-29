package com.cmeza.sdgenerator.util;

import lombok.Builder;
import lombok.Data;
import org.apache.maven.project.MavenProject;

import java.util.Set;

/**
 * Created by carlos on 29/11/23.
 */

@Data
@Builder
public class GeneratorProperties {
    private String[] entityPackage;
    private String repositoryPackage;
    private String repositoryPostfix;
    private String managerPackage;
    private String managerPostfix;
    private boolean onlyAnnotations;
    private boolean overwrite;
    private boolean lombokAnnotations;
    private boolean withComments;
    private boolean withJpaSpecificationExecutor;
    private Set<String> additionalExtendsList;
    private MavenProject project;
}
