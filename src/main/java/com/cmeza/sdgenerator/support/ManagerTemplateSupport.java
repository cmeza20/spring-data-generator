package com.cmeza.sdgenerator.support;

import com.cmeza.sdgenerator.provider.AbstractTemplateProvider;
import com.cmeza.sdgenerator.support.maker.ManagerStructure;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

/**
 * Created by carlos on 08/04/17.
 */
public class ManagerTemplateSupport extends AbstractTemplateProvider {

    private String repositoryPackage;
    private String repositoryPostfix;

    public ManagerTemplateSupport(AnnotationAttributes attributes, String repositoryPackage, String repositoryPostfix) {
        super(attributes);
        this.repositoryPackage = repositoryPackage;
        this.repositoryPostfix = repositoryPostfix;
    }

    @Override
    protected String getContentFromTemplate(String mPackage, String simpleClassName, String postfix, BeanDefinition beanDefinition) {
        return new ManagerStructure(mPackage, simpleClassName, postfix, repositoryPackage, repositoryPostfix).build();
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