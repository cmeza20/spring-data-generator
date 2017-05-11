package com.cmeza.sdgenerator.support.maker;

import com.cmeza.sdgenerator.support.maker.builder.ObjectBuilder;
import com.cmeza.sdgenerator.support.maker.builder.ObjectStructure;
import com.cmeza.sdgenerator.support.maker.values.ObjectTypeValues;
import com.cmeza.sdgenerator.support.maker.values.ScopeValues;
import com.cmeza.sdgenerator.util.CustomResourceLoader;
import com.cmeza.sdgenerator.util.SDLogger;
import com.cmeza.sdgenerator.util.Tuple;

import javax.persistence.Id;
import java.lang.reflect.Field;

/**
 * Created by carlos on 08/04/17.
 */
public class RepositoryStructure {

    private static CustomResourceLoader loader;
    private ObjectBuilder objectBuilder;
    private static Integer error = 0;

    public RepositoryStructure(String repositoryPackage, String entityName, String entityClass, String postfix, CustomResourceLoader loader) {
        this.loader = loader;
        String repositoryName = entityName + postfix;
        String entityId = getEntityId(entityClass);
        if(entityId != null) {
            this.objectBuilder = new ObjectBuilder(
                    new ObjectStructure(repositoryPackage, ScopeValues.PUBLIC, ObjectTypeValues.INTERFACE, repositoryName)
                            .addImport(entityClass)
                            .addImport("org.springframework.data.jpa.repository.JpaRepository")
                            .addImport("org.springframework.stereotype.Repository")
                            .addAnnotation("Repository")
                            .setExtend("JpaRepository", entityName, entityId)
            );
        }
    }

    @SuppressWarnings("unchecked")
    private static String getEntityId(String entityClass){
        try{
            Class<?> entity = null;
            if (loader == null) {
                entity = Class.forName(entityClass);
            } else {
                entity = loader.getUrlClassLoader().loadClass(entityClass);
            }

            for (Field field : entity.getDeclaredFields()) {

                if (field.isAnnotationPresent(Id.class)){
                    return field.getType().getSimpleName();
                }
            }

            error = SDLogger.addError("Repository Error: @Id not found: " + entityClass);
            return null;
        }catch (Exception e) {
            error = SDLogger.addError( "Repository Error: Failed to access entity" + entityClass);
            return null;
        }
    }

    public Tuple<String, Integer> build(){
        return new Tuple<>(objectBuilder == null ? null : objectBuilder.build(), error);
    }
}
