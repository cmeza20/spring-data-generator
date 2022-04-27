package com.cmeza.sdgenerator.support.maker;

import com.cmeza.sdgenerator.support.maker.builder.ObjectBuilder;
import com.cmeza.sdgenerator.support.maker.builder.ObjectStructure;
import com.cmeza.sdgenerator.support.maker.values.ExpressionValues;
import com.cmeza.sdgenerator.support.maker.values.ObjectTypeValues;
import com.cmeza.sdgenerator.support.maker.values.ObjectValues;
import com.cmeza.sdgenerator.support.maker.values.ScopeValues;
import com.cmeza.sdgenerator.util.GeneratorUtils;
import com.cmeza.sdgenerator.util.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by carlos on 08/04/17.
 */
public class ManagerStructure {

    private final ObjectBuilder objectBuilder;

    public ManagerStructure(String managerPackage, String entityName, String entityClass, String postfix, String repositoryPackage, String repositoryPostfix, String additionalPackage, boolean lombokAnnotations, boolean withComments) {

        String managerName = entityName + postfix;
        String repositoryName = entityName + repositoryPostfix;
        String repositoryNameAttribute = GeneratorUtils.decapitalize(repositoryName);

        this.objectBuilder = new ObjectBuilder(new ObjectStructure(managerPackage, ScopeValues.PUBLIC, ObjectTypeValues.CLASS, managerName)
                .setLombokAnnotations(lombokAnnotations)
                .addImport(repositoryPackage + "." + (additionalPackage.isEmpty() ? "" : (additionalPackage + ".")) + repositoryName)
                .addImport(entityClass)
                .addImport(Autowired.class)
                .addImport(Component.class)
                .addAnnotation(Component.class)
                .addFinalAttribute(repositoryName, repositoryNameAttribute)
                .addConstructor(new ObjectStructure.ObjectConstructor(ScopeValues.PUBLIC, managerName)
                        .addAnnotation(Autowired.class)
                        .addArgument(repositoryName, repositoryNameAttribute)
                        .addBodyLine(ObjectValues.THIS.getValue() + repositoryNameAttribute + ExpressionValues.EQUAL.getValue() + repositoryNameAttribute)
                )
        )
                .setAttributeBottom(false)
                .setWithComments(withComments);

    }

    public Tuple<String, Integer> build() {
        return new Tuple<>(objectBuilder == null ? null : objectBuilder.build(), 0);
    }
}
