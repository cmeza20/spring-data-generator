package com.cmeza.sdgenerator.provider;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * Created by carlos on 08/04/17.
 */
public class ClassPathScanningProvider extends ClassPathScanningCandidateComponentProvider {

    private Class<?> classComparator;

    public ClassPathScanningProvider() {
        super(false);
    }

    public void setIncludeAnnotation(Class<? extends Annotation> annotation) {
        this.classComparator = annotation;
        super.addIncludeFilter(new AnnotationTypeFilter(annotation));
    }

    public void setExcludeAnnotation(Class<? extends Annotation> annotation) {
        super.addExcludeFilter(new AnnotationTypeFilter(annotation));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        if (classComparator == null) {
            return false;
        }

        boolean isNonRepositoryInterface = !classComparator.getName().equals(beanDefinition.getBeanClassName());
        boolean isTopLevelType = !beanDefinition.getMetadata().hasEnclosingClass();
        return isNonRepositoryInterface && isTopLevelType;
    }
}
