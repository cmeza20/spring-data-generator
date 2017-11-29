package com.cmeza.sdgenerator.support.maker;

import com.cmeza.sdgenerator.support.maker.builder.ObjectBuilder;
import com.cmeza.sdgenerator.support.maker.builder.ObjectStructure;
import com.cmeza.sdgenerator.support.maker.values.ObjectTypeValues;
import com.cmeza.sdgenerator.support.maker.values.ScopeValues;
import com.cmeza.sdgenerator.util.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by carlos on 08/04/17.
 */
public class RepositoryStructure {

    private CustomResourceLoader loader;
    private ObjectBuilder objectBuilder;
    private Integer error = 0;

    public RepositoryStructure(String repositoryPackage, String entityName, String entityClass, String postfix, CustomResourceLoader loader) {
        this.loader = loader;
        String repositoryName = entityName + postfix;
        Tuple<String, Boolean> entityId = getEntityId(entityClass);
        if(entityId != null) {
            this.objectBuilder = new ObjectBuilder(
                    new ObjectStructure(repositoryPackage, ScopeValues.PUBLIC, ObjectTypeValues.INTERFACE, repositoryName)
                            .addImport(entityClass)
                            .addImport("org.springframework.data.jpa.repository.JpaRepository")
                            .addImport("org.springframework.data.jpa.repository.JpaSpecificationExecutor")
                            .addImport("org.springframework.stereotype.Repository")
                            .addImport(entityId.right() ? entityId.left() : "")
                            .addAnnotation("Repository")
                            .setExtend("JpaRepository", entityName, GeneratorUtils.getSimpleClassName(entityId.left()))
                            .setExtend("JpaSpecificationExecutor", entityName)
            );
        }
    }

    @SuppressWarnings("unchecked")
    private Tuple<String, Boolean> getEntityId(String entityClass){
        try {
            Class<?> entity = null;
            if (loader == null) {
                entity = Class.forName(entityClass);
            } else {
                entity = loader.getUrlClassLoader().loadClass(entityClass);
            }

            for (Field field : entity.getDeclaredFields()) {

                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
                    this.implementsSerializable(field.getType());
                    return new Tuple<>(field.getType().getName(), this.isCustomType(field.getType()));
                }
            }

            for (Method method : entity.getDeclaredMethods()) {
                if (!method.getReturnType().equals(Void.TYPE) && (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(EmbeddedId.class))) {
                    this.implementsSerializable(method.getReturnType());
                    return new Tuple<>(method.getReturnType().getName(), this.isCustomType(method.getReturnType()));
                }
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
        if (clazz.isAssignableFrom(Boolean.class) ||
                clazz.isAssignableFrom(Byte.class) ||
                clazz.isAssignableFrom(String.class) ||
                clazz.isAssignableFrom(Integer.class) ||
                clazz.isAssignableFrom(Long.class) ||
                clazz.isAssignableFrom(Float.class) ||
                clazz.isAssignableFrom(Double.class)){
            return false;
        }
        return true;
    }

    private void implementsSerializable(Class<?> clazz) throws GeneratorException{
        if (!Serializable.class.isAssignableFrom(clazz)) {
            throw new GeneratorException("Type parameter '" + clazz.getName() + "' should implement 'java.io.Serializable'");
        }
    }
}
