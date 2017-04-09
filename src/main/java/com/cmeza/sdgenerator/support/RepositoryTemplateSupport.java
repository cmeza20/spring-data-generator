package com.cmeza.sdgenerator.support;

import com.cmeza.sdgenerator.provider.AbstractTemplateProvider;
import com.cmeza.sdgenerator.support.maker.RepositoryStructure;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

/**
 * Created by carlos on 08/04/17.
 */
public class RepositoryTemplateSupport extends AbstractTemplateProvider {

    public RepositoryTemplateSupport(AnnotationAttributes attributes) {
        super(attributes);
    }

    @Override
    protected String getContentFromTemplate(String repositoryPackage, String simpleClassName, String postfix, BeanDefinition beanDefinition) {
        return new RepositoryStructure(repositoryPackage, simpleClassName, beanDefinition.getBeanClassName(), postfix).build();
    }

    @Override
    protected String getExcludeClasses() {
        return "excludeRepositoriesClasses";
    }

    @Override
    protected String getPostfix() {
        return "repositoryPostfix";
    }

}
