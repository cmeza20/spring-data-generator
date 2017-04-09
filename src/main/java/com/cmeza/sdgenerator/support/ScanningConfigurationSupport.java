package com.cmeza.sdgenerator.support;

import com.cmeza.sdgenerator.provider.ClassPathScanningProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.*;

/**
 * Created by carlos on 08/04/17.
 */
public class ScanningConfigurationSupport {

    private final Environment environment;
    private final AnnotationAttributes attributes;
    private final AnnotationMetadata annotationMetadata;

    public ScanningConfigurationSupport(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes, Environment environment){
        Assert.notNull(environment, "Environment must not be null!");
        Assert.notNull(environment, "AnnotationMetadata must not be null!");
        this.environment = environment;
        this.attributes = attributes;
        this.annotationMetadata = annotationMetadata;
    }

    @SuppressWarnings("unchecked")
    private Iterable<String> getBasePackages() {

        String[] entityPackage = this.attributes.getStringArray("entityPackage");

        if (entityPackage.length == 0) {
            String className = this.annotationMetadata.getClassName();
            return Collections.singleton(ClassUtils.getPackageName(className));
        } else {
            HashSet packages = new HashSet();
            packages.addAll(Arrays.asList(entityPackage));

            return packages;
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<BeanDefinition> getCandidates(ResourceLoader resourceLoader) {
        if(this.getBasePackages() == null){
            return Collections.emptyList();
        }

        ClassPathScanningProvider scanner = new ClassPathScanningProvider();
        scanner.setResourceLoader(resourceLoader);
        scanner.setEnvironment(this.environment);

        Iterator filterPackages = this.getBasePackages().iterator();

        HashSet candidates = new HashSet();

        while(filterPackages.hasNext()) {
            String basePackage = (String)filterPackages.next();
            Set candidate = scanner.findCandidateComponents(basePackage);
            candidates.addAll(candidate);
        }

        return candidates;
    }
}

