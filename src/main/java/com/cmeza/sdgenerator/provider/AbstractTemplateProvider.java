package com.cmeza.sdgenerator.provider;

import com.cmeza.sdgenerator.annotation.SDGenerator;
import com.cmeza.sdgenerator.util.GeneratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.Assert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by carlos on 08/04/17.
 */
public abstract class AbstractTemplateProvider {

    private final static Log logger = LogFactory.getLog(SDGenerator.class);
    private Class<?>[] excludeClasses;
    private String postfix;
    private boolean debug;

    public AbstractTemplateProvider(AnnotationAttributes attributes) {
        Assert.notNull(attributes, "AnnotationAttributes must not be null!");
        this.excludeClasses = attributes.getClassArray(getExcludeClasses());
        this.postfix = attributes.getString(getPostfix());
        this.debug = attributes.getBoolean("debug");
        if (excludeClasses.length > 0 && debug) {
            logger.debug(String.format("Exclude %s %s in the %s generator", excludeClasses.length, excludeClasses.length == 1 ? "entity":"entities", postfix));
        }
    }

    public boolean initializeCreation(String path, String ePackage, Collection<BeanDefinition> candidates) {
        boolean result = true;
        int generatedCount = 0;
        if(GeneratorUtils.verifyPackage(path)){
            if (debug) {
                logger.info(String.format(" %1$-84s ", "-").replace(' ', '-'));
                logger.info(String.format("|  %1$-12s |  %2$-55s | %3$-6s  |", "postfix", "File Name", "Result"));
                logger.info(String.format("| %1$-82s |", "-").replace(' ', '-'));
            }
            for (BeanDefinition beanDefinition : candidates) {
                if (!verifyEntityNonExclude(beanDefinition.getBeanClassName())){
                    boolean creation = createHelper(path, beanDefinition, postfix, ePackage);
                    if (!creation) {
                        result = false;
                    } else {
                        generatedCount++;
                    }
                }
            }
            if (debug) {
                if (generatedCount == 0) {
                    logger.info(String.format("|  %1$-81s |", "No " + postfix.toLowerCase() + " generated"));
                }
                logger.info(String.format(" %1$-84s ", "-").replace(' ', '-'));
                logger.info("");
            } else {
                logger.info(String.format("Generated %s %s files", generatedCount, postfix.toLowerCase()));
            }

        }
        return result;
    }

    private boolean verifyEntityNonExclude(String beanClassName){
        return Arrays.stream(excludeClasses).anyMatch(b -> b.getName().equals(beanClassName));
    }

    private boolean createHelper(String path, BeanDefinition beanDefinition, String postfix, String repositoryPackage){
        String simpleClassName = GeneratorUtils.getSimpleClassName(beanDefinition.getBeanClassName());
        boolean result = false;
        if(simpleClassName != null){

            String fileHelper = simpleClassName + postfix + ".java";
            String filePath = path + "/" + fileHelper;

            File file = new File(filePath);
            if (!file.exists()){
                result = createFileFromTemplate(filePath, repositoryPackage, simpleClassName, postfix, beanDefinition);
                if (debug){
                    logger.info(String.format("|  %1$-12s |  %2$-55s |  %3$-5s  |", postfix, fileHelper, result));
                }
            }
        } else {
            logger.warn(String.format("Could not get SimpleName from: %s", beanDefinition.getBeanClassName()));
        }

        return result;
    }

    protected abstract String getContentFromTemplate(String mPackage, String simpleClassName, String postfix, BeanDefinition beanDefinition);

    private boolean createFileFromTemplate(String path, String repositoryPAckage, String simpleClassName, String postfix, BeanDefinition beanDefinition){
        String content = getContentFromTemplate(repositoryPAckage, simpleClassName, postfix, beanDefinition);
        if (content == null) {
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(content);
            return true;
        } catch (IOException e) {
            logger.error("Error occurred while persisting file: " + e.getMessage());
            return false;
        }
    }

    protected abstract String getExcludeClasses();
    protected abstract String getPostfix();

}
