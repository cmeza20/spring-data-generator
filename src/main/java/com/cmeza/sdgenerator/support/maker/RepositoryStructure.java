package com.cmeza.sdgenerator.support.maker;

import com.cmeza.sdgenerator.support.maker.builder.ObjectBuilder;
import com.cmeza.sdgenerator.support.maker.builder.ObjectStructure;
import com.cmeza.sdgenerator.support.maker.values.ObjectTypeValues;
import com.cmeza.sdgenerator.support.maker.values.ScopeValues;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by carlos on 08/04/17.
 */
public class RepositoryStructure {

    private static final Log logger = LogFactory.getLog(RepositoryStructure.class);
    private ObjectBuilder objectBuilder;

    public RepositoryStructure(String repositoryPackage, String entityName, String entityClass, String postfix) {
        String repositoryName = entityName + postfix;
        String entityId = getEntityId(entityClass);
        if(entityId != null) {

            this.objectBuilder = new ObjectBuilder(new ObjectStructure(repositoryPackage, ScopeValues.PUBLIC, ObjectTypeValues.INTERFACE, repositoryName)
                    .addImport(entityClass)
                    .addImport("org.springframework.data.jpa.repository.JpaRepository")
                    .setExtend("JpaRepository", entityName, entityId)
            );

        }
    }

    @SuppressWarnings("unchecked")
    private static String getEntityId(String entityClass){
        try{
            Class<?> entity = Class.forName(entityClass);

            for (Field field : entity.getDeclaredFields()) {
                if (field.isAnnotationPresent((Class<? extends Annotation>) Class.forName("javax.persistence.Id"))){
                    return field.getType().getSimpleName();
                }
            }

            logger.error("@Id not found: " + entityClass);
            return null;
        }catch (Exception e) {
            logger.error( "Failed to access entity" + entityClass);
            return null;
        }
    }

    public String build(){
        return objectBuilder == null ? null : objectBuilder.build();
    }
}
