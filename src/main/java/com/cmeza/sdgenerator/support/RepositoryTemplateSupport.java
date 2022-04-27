package com.cmeza.sdgenerator.support;

import com.cmeza.sdgenerator.provider.AbstractTemplateProvider;
import com.cmeza.sdgenerator.support.maker.RepositoryStructure;
import com.cmeza.sdgenerator.util.CustomResourceLoader;
import com.cmeza.sdgenerator.util.Tuple;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.Set;

/**
 * Created by carlos on 08/04/17.
 */
public class RepositoryTemplateSupport extends AbstractTemplateProvider {

    private CustomResourceLoader loader;
    private final Set<String> additionalExtends;
    private final boolean withComments;

    public RepositoryTemplateSupport(AnnotationAttributes attributes, Set<String> additionalExtends, boolean withComments) {
        super(attributes);
        this.additionalExtends = additionalExtends;
        this.withComments = withComments;
    }

    public RepositoryTemplateSupport(CustomResourceLoader loader, Set<String> additionalExtends, boolean withComments) {
        super(loader);
        this.loader = loader;
        this.additionalExtends = additionalExtends;
        this.withComments = withComments;
    }

    @Override
    protected Tuple<String, Integer> getContentFromTemplate(String repositoryPackage, String simpleClassName, String postfix, BeanDefinition beanDefinition, String additionalPackage) {
        return new RepositoryStructure(repositoryPackage, simpleClassName, beanDefinition.getBeanClassName(), postfix, loader, additionalExtends, withComments).build();
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
