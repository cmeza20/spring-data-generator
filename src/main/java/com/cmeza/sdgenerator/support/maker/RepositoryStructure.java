package com.cmeza.sdgenerator.support.maker;

import com.cmeza.sdgenerator.support.maker.builder.ObjectBuilder;
import com.cmeza.sdgenerator.support.maker.builder.ObjectStructure;
import com.cmeza.sdgenerator.support.maker.values.ObjectTypeValues;
import com.cmeza.sdgenerator.support.maker.values.ScopeValues;
import com.cmeza.sdgenerator.util.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by carlos on 08/04/17.
 */
public class RepositoryStructure {

    private CustomResourceLoader loader;
    private ObjectBuilder objectBuilder;
    private Integer error = 0;
    private final static Map<Class<?>, Class<?>> mapConvert = new HashMap<>();
    static {
        mapConvert.put(boolean.class, Boolean.class);
        mapConvert.put(byte.class, Byte.class);
        mapConvert.put(short.class, Short.class);
        mapConvert.put(char.class, Character.class);
        mapConvert.put(int.class, Integer.class);
        mapConvert.put(long.class, Long.class);
        mapConvert.put(float.class, Float.class);
        mapConvert.put(double.class, Double.class);
    }

    public RepositoryStructure(String repositoryPackage, String entityName, String entityClass, String postfix, CustomResourceLoader loader, Set<String> additionalExtends) {
        this.loader = loader;
        String repositoryName = entityName + postfix;
        Tuple<String, Boolean> entityId = getEntityId(entityClass);
        if(entityId != null) {

            ObjectStructure objectStructure = new ObjectStructure(repositoryPackage, ScopeValues.PUBLIC, ObjectTypeValues.INTERFACE, repositoryName)
                    .addImport(entityClass)
                    .addImport("org.springframework.data.jpa.repository.JpaRepository")
                    .addImport("org.springframework.data.jpa.repository.JpaSpecificationExecutor")
                    .addImport("org.springframework.stereotype.Repository")
                    .addImport(entityId.right() ? entityId.left() : "")
                    .addAnnotation("Repository")
                    .addExtend("JpaRepository", entityName, GeneratorUtils.getSimpleClassName(entityId.left()))
                    .addExtend("JpaSpecificationExecutor", entityName);

            if (additionalExtends != null) {
                for(String additionalExtend : additionalExtends) {
                    objectStructure.addImport(additionalExtend);
                    objectStructure.addExtend(GeneratorUtils.getSimpleClassName(additionalExtend), entityName);
                }
            }
            this.objectBuilder = new ObjectBuilder(objectStructure);
        }
    }

    @SuppressWarnings("unchecked")
    private Tuple<String, Boolean> getEntityId(String entityClass){
        try {
            Class<?> entity;
            if (loader == null) {
                entity = Class.forName(entityClass);
            } else {
                entity = loader.getUrlClassLoader().loadClass(entityClass);
            }

            while (entity != null){
                for (Field field : entity.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
                        Class<?> dataType = field.getType();
                        if (field.getType().isPrimitive()) {
                            dataType = this.primitiveToObject(field.getType());
                        }
                        return new Tuple<>(dataType.getName(), this.isCustomType(dataType));
                    }
                }

                for (Method method : entity.getDeclaredMethods()) {
                    if (!method.getReturnType().equals(Void.TYPE) && (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(EmbeddedId.class))) {
                        Class<?> dataType = method.getReturnType();
                        if (method.getReturnType().isPrimitive()) {
                            dataType = this.primitiveToObject(method.getReturnType());
                        }
                        return new Tuple<>(dataType.getName(), this.isCustomType(dataType));
                    }
                }
                entity = entity.getSuperclass();
            }

            error = SDLogger.addError("Repository Error: Primary key not found in " + GeneratorUtils.getSimpleClassName(entityClass) + ".java");
            return null;
        } catch (GeneratorException ex) {
            error = SDLogger.addError(ex.getMessage());
            return null;
        } catch (Exception e) {
            error = SDLogger.addError("Repository Error: Failed to access entity " + GeneratorUtils.getSimpleClassName(entityClass) + ".java");
            return null;
        }
    }

    public Tuple<String, Integer> build(){
        return new Tuple<>(objectBuilder == null ? null : objectBuilder.build(), error);
    }

    private boolean isCustomType(Class<?> clazz) {
        return  !clazz.isAssignableFrom(Boolean.class) &&
                !clazz.isAssignableFrom(Byte.class) &&
                !clazz.isAssignableFrom(String.class) &&
                !clazz.isAssignableFrom(Integer.class) &&
                !clazz.isAssignableFrom(Long.class) &&
                !clazz.isAssignableFrom(Float.class) &&
                !clazz.isAssignableFrom(Double.class);
    }

    private Class<?> primitiveToObject(Class<?> clazz) {
        Class<?> convertResult = mapConvert.get(clazz);
        if (convertResult == null) {
            throw new GeneratorException("Type parameter '" + clazz.getName() + "' is incorrect");
        }
        return convertResult;
    }

}
