package com.cmeza.sdgenerator.provider;

import com.cmeza.sdgenerator.util.CustomResourceLoader;
import com.cmeza.sdgenerator.util.GeneratorUtils;
import com.cmeza.sdgenerator.util.SDLogger;
import com.cmeza.sdgenerator.util.Tuple;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by carlos on 08/04/17.
 */
public abstract class AbstractTemplateProvider {

    private Class<?>[] excludeClasses;
    private String postfix;
    private boolean debug;
    private Collection<File> includeFilter;
    private String includeFilterPostfix = "";
    private boolean overwrite;

    public AbstractTemplateProvider(AnnotationAttributes attributes) {
        Assert.notNull(attributes, "AnnotationAttributes must not be null!");
        this.excludeClasses = attributes.getClassArray(getExcludeClasses());
        this.postfix = attributes.getString(getPostfix());
        this.debug = attributes.getBoolean("debug");
        this.overwrite = attributes.getBoolean("overwrite");
        if (excludeClasses.length > 0 && debug) {
            SDLogger.debug(String.format("Exclude %s %s in the %s generator", excludeClasses.length, excludeClasses.length == 1 ? "entity":"entities", postfix));
        }
    }

    public AbstractTemplateProvider(CustomResourceLoader customResourceLoader) {
        Assert.notNull(customResourceLoader, "CustomResourceLoader must not be null!");
        this.postfix = customResourceLoader.getPostfix();
        this.debug = true;
        this.excludeClasses = new Class[]{};
        this.overwrite = customResourceLoader.isOverwrite();
    }

    public void initializeCreation(String path, String ePackage, Collection<BeanDefinition> candidates, String[] entityPackage) {
        int generatedCount = 0;

        if(!GeneratorUtils.verifyPackage(path)){
            return;
        }

        Arrays.sort(entityPackage, Comparator.comparingInt((c) -> c.length()));

        for (BeanDefinition beanDefinition : candidates) {

            if (verifyEntityNonExclude(beanDefinition.getBeanClassName())){
                continue;
            }

            if (createHelper(path, beanDefinition, postfix, ePackage, entityPackage)) {
                generatedCount++;
            }
        }

        SDLogger.plusGenerated(generatedCount);
    }

    protected void setIncludeFilter(Collection<File> includeFilter){
        this.includeFilter = includeFilter;
    }

    protected void setIncludeFilterPostfix(String includeFilterPostfix) {
        this.includeFilterPostfix = includeFilterPostfix;
    }

    private Tuple<Boolean, Integer> verifyIncludeFilter(String beanDefinitionName) {
        int warnPosition = 0;

        if (includeFilter == null) {
            return new Tuple<>(Boolean.TRUE, warnPosition);
        }

        boolean result = includeFilter.stream().anyMatch(i ->
            i.getName().replace(".java", "")
                    .equals(beanDefinitionName + includeFilterPostfix)
        );

        if (!result) {
            warnPosition = SDLogger.addWarn(String.format("%s ignored: Repository not found for %s entity class", postfix, beanDefinitionName));
        }
        return new Tuple<>(result, warnPosition);
    }

    private boolean verifyEntityNonExclude(String beanClassName){
        return Arrays.stream(excludeClasses).anyMatch(b -> b.getName().equals(beanClassName));
    }

    private boolean createHelper(String path, BeanDefinition beanDefinition, String postfix, String repositoryPackage, String[] entityPackage){
        String simpleClassName = GeneratorUtils.getSimpleClassName(beanDefinition.getBeanClassName());
        Tuple<Boolean, Integer> result = null;
        if(simpleClassName != null){

            String fileHelper = simpleClassName + postfix + ".java";

            String additionalPath = this.getAdditionalPath(entityPackage, beanDefinition,  simpleClassName, path);
            String additionalPackage = "";
            if (!StringUtils.isEmpty(additionalPath)) {
                repositoryPackage += "." + additionalPath;
                additionalPackage = additionalPath;
                additionalPath = additionalPath.replace(".", "/") + "/";
            }
            String filePath = path + "/" + additionalPath + fileHelper;

            Tuple<Boolean, Integer> verifyInclude = verifyIncludeFilter(simpleClassName);
            if (!verifyInclude.left()) {
                SDLogger.addRowGeneratedTable(postfix, fileHelper, "Warn #" + verifyInclude.right());
                return false;
            }

            File file = new File(filePath);

            String fileCondition = "Created";
            if (overwrite && file.exists()) {
                file.delete();
                fileCondition = "Overwritten";
            }

            if (!file.exists()){
                result = createFileFromTemplate(filePath, repositoryPackage, simpleClassName, postfix, beanDefinition, additionalPackage);
                if (debug){
                    SDLogger.addRowGeneratedTable(postfix, fileHelper, result.left() ? fileCondition : "Error #" + result.right());
                }
            }
        } else {
            SDLogger.addError(String.format("Could not get SimpleName from: %s", beanDefinition.getBeanClassName()));
        }

        return result == null ? false : result.left();
    }

    protected abstract Tuple<String, Integer> getContentFromTemplate(String mPackage, String simpleClassName, String postfix, BeanDefinition beanDefinition, String additionalPackage);

    private Tuple<Boolean, Integer> createFileFromTemplate(String path, String repositoryPackage, String simpleClassName, String postfix, BeanDefinition beanDefinition, String additionalPackage){
        Tuple<String, Integer> content = getContentFromTemplate(repositoryPackage, simpleClassName, postfix, beanDefinition, additionalPackage);
        if (content.left() == null) {
            return new Tuple<>(false, content.right());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(content.left());
            return new Tuple<>(true, 0);
        } catch (IOException e) {
            return new Tuple<>(false, SDLogger.addError("Error occurred while persisting file: " + e.getMessage()));
        }
    }

    protected abstract String getExcludeClasses();
    protected abstract String getPostfix();

    private String getAdditionalPath(String[] entityPackages, BeanDefinition beanDefinition, String filename, String path) {
        List<String> additionalFolders = new ArrayList<>();
        for (String entityPackage : entityPackages) {
            if (beanDefinition.getBeanClassName().startsWith(entityPackage)) {
                String stack = beanDefinition.getBeanClassName().replace(entityPackage, "").replace(filename, "");
                if (stack.length() > 1) {
                    if (stack.startsWith(".")) {
                        stack = stack.substring(1);
                    }
                    if (stack.endsWith(".")) {
                        stack = stack.substring(0, stack.length() - 1);
                    }
                    additionalFolders.add(stack);
                }
            }
        }

        if (!additionalFolders.isEmpty()) {
            additionalFolders.sort((a, b)-> Integer.compare(b.length(), a.length()));
            String additional = path + "/" + additionalFolders.get(0);
            String pathAdditional = additional.replace(".", "/");
            GeneratorUtils.verifyPackage(pathAdditional);
            return additionalFolders.get(0);
        }
        return "";
    }
}
