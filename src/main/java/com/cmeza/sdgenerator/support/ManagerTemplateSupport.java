package com.cmeza.sdgenerator.support;

import com.cmeza.sdgenerator.provider.AbstractTemplateProvider;
import com.cmeza.sdgenerator.support.maker.ManagerStructure;
import com.cmeza.sdgenerator.util.CustomResourceLoader;
import com.cmeza.sdgenerator.util.GeneratorUtils;
import com.cmeza.sdgenerator.util.Tuple;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

import java.io.File;
import java.util.Arrays;

/**
 * Created by carlos on 08/04/17.
 */
public class ManagerTemplateSupport extends AbstractTemplateProvider {

    private final String repositoryPackage;
    private final String repositoryPostfix;
    private final boolean lombokAnnotations;
    private final boolean withComments;

    public ManagerTemplateSupport(AnnotationAttributes attributes, String repositoryPackage, String repositoryPostfix, boolean lombokAnnotations, boolean withComments) {
        super(attributes);
        this.repositoryPackage = repositoryPackage;
        this.repositoryPostfix = repositoryPostfix;
        this.lombokAnnotations = lombokAnnotations;
        this.withComments = withComments;
        this.findFilterRepositories();
    }

    public ManagerTemplateSupport(CustomResourceLoader customResourceLoader, boolean lombokAnnotations, boolean withComments) {
        super(customResourceLoader);
        this.repositoryPackage = customResourceLoader.getRepositoryPackage();
        this.repositoryPostfix = customResourceLoader.getRepositoryPostfix();
        this.lombokAnnotations = lombokAnnotations;
        this.withComments = withComments;
        this.findFilterRepositories();
    }

    private void findFilterRepositories() {
        String repositoryPath = GeneratorUtils.getAbsolutePath(repositoryPackage);
        File[] repositoryFiles = GeneratorUtils.getFileList(repositoryPath, repositoryPostfix);
        this.setIncludeFilter(Arrays.asList(repositoryFiles));
        this.setIncludeFilterPostfix(repositoryPostfix);
    }

    @Override
    protected Tuple<String, Integer> getContentFromTemplate(String mPackage, String simpleClassName, String postfix, BeanDefinition beanDefinition, String additionalPackage) {
        return new ManagerStructure(mPackage, simpleClassName, beanDefinition.getBeanClassName(), postfix, repositoryPackage, repositoryPostfix, additionalPackage, lombokAnnotations, withComments).build();
    }

    @Override
    protected String getExcludeClasses() {
        return "excludeManagerClasses";
    }

    @Override
    protected String getPostfix() {
        return "managerPostfix";
    }

}