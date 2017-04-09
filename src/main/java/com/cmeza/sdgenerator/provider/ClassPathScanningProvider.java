package com.cmeza.sdgenerator.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * Created by carlos on 08/04/17.
 */
public class ClassPathScanningProvider extends ClassPathScanningCandidateComponentProvider {

    private final static String entityClass = "javax.persistence.Entity";
    private final static Log logger = LogFactory.getLog(ClassPathScanningProvider.class);

    @SuppressWarnings("unchecked")
    public ClassPathScanningProvider() {
        super(false);
        try {
            super.addIncludeFilter(new AnnotationTypeFilter((Class<? extends Annotation>) Class.forName(entityClass)));
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isNonRepositoryInterface = !entityClass.equals(beanDefinition.getBeanClassName());
        boolean isTopLevelType = !beanDefinition.getMetadata().hasEnclosingClass();
        return isNonRepositoryInterface && isTopLevelType;
    }
}
