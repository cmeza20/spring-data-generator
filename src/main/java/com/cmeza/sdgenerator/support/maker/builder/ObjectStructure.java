package com.cmeza.sdgenerator.support.maker.builder;

import com.cmeza.sdgenerator.support.maker.values.CommonValues;
import com.cmeza.sdgenerator.support.maker.values.ObjectTypeValues;
import com.cmeza.sdgenerator.support.maker.values.ObjectValues;
import com.cmeza.sdgenerator.support.maker.values.ScopeValues;
import com.cmeza.sdgenerator.util.BuildUtils;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by carlos on 08/04/17.
 */
public class ObjectStructure {

    private ObjectTypeValues objectType;
    private ScopeValues objectScope;

    private String objectPackage;
    private Set<String> objectImports = new LinkedHashSet<>();
    private Set<String> objectAnnotations = new LinkedHashSet<>();
    private Set<String> objectImplements = new LinkedHashSet<>();
    private Set<String> objectAttributes = new LinkedHashSet<>();
    private Set<ObjectMethod> objectMethods = new LinkedHashSet<>();
    private Set<ObjectFunction> objectFunctions = new LinkedHashSet<>();
    private Set<ObjectConstructor> objectConstructors = new LinkedHashSet<>();
    private Set<String> objectExtends = new LinkedHashSet<>();
    private String objectName;
    private String objectRawBody;

    public ObjectStructure(String objectPackage, ScopeValues objectScope, ObjectTypeValues objectType, String objectName) {
        this.objectPackage = BuildUtils.buildPackage(objectPackage);
        this.objectScope = objectScope;
        this.objectType = objectType;
        this.objectName = BuildUtils.cleanSpaces(objectName);
//        this.objectExtend = "";
        this.objectRawBody = "";
    }

    public ObjectStructure setPackage(String objectPackage) {
        if (!objectPackage.isEmpty()) {
            this.objectPackage = BuildUtils.buildPackage(objectPackage);
        }
        return this;
    }

    public ObjectStructure addImport(String objectImport) {
        if (!objectImport.isEmpty()) {
            this.objectImports.add(BuildUtils.buildImport(objectImport));
        }
        return this;
    }

    public ObjectStructure addImport(Class<?> clazz) {
        if (clazz != null) {
            this.objectImports.add(BuildUtils.buildImport(clazz.getName()));
        }
        return this;
    }

    public ObjectStructure addAnnotation(String annotation) {
        if (!annotation.isEmpty()) {
            this.objectAnnotations.add(BuildUtils.buildAnnotation(annotation));
        }
        return this;
    }

    public ObjectStructure addAnnotation(Class<?> clazz) {
        if (clazz != null) {
            this.objectAnnotations.add(BuildUtils.buildAnnotation(clazz.getSimpleName()));
        }
        return this;
    }

    public ObjectStructure addImplement(String objectImplement, Object... objects) {
        this.objectImplements.add(BuildUtils.builDiamond(objectImplement, objects));
        return this;
    }

    public ObjectStructure addImplement(Class<?> clazz, Object... objects) {
        if (clazz != null) {
            this.objectImplements.add(BuildUtils.builDiamond(clazz.getSimpleName(), objects));
        }
        return this;
    }

    public ObjectStructure addExtend(String objectExtend, Object... objects) {
        this.objectExtends.add(BuildUtils.builDiamond(objectExtend, objects));
        return this;
    }

    public ObjectStructure addExtend(Class<?> clazz, Object... objects) {
        this.objectExtends.add(BuildUtils.builDiamond(clazz.getSimpleName(), objects));
        return this;
    }

    public ObjectStructure setObjectRawBody(String objectRawBody) {
        this.objectRawBody = objectRawBody;
        return this;
    }

    public ObjectStructure addConstructor(ObjectConstructor objectConstructor) {
        if (objectConstructor != null) {
            this.objectConstructors.add(objectConstructor);
        }
        return this;
    }

    public ObjectStructure addAttribute(String attributeClass, String attribute) {
        this.objectAttributes.add(BuildUtils.buildAttribute(attributeClass, attribute));
        return this;
    }

    public ObjectStructure addMethod(ObjectMethod objectMethod) {
        this.objectMethods.add(objectMethod);
        return this;
    }

    public ObjectStructure addFunction(ObjectFunction objectFunction) {
        this.objectFunctions.add(objectFunction);
        return this;
    }

    public ObjectTypeValues getObjectType() {
        return objectType;
    }

    public ScopeValues getObjectScope() {
        return objectScope;
    }

    public String getObjectPackage() {
        return objectPackage;
    }

    public String getObjectImports() {
        List<String> objectImportsOrder = new LinkedList<>(objectImports);
        objectImportsOrder.sort(Comparator.comparing(String::toString));
        StringBuilder concat = new StringBuilder("");
        for (String str : objectImportsOrder) {
            concat.append(str);
        }
        if (!objectImportsOrder.isEmpty()) {
            concat.append(CommonValues.NEWLINE);
        }
        return concat.toString();
    }

    public String getObjectAnnotations() {
        StringBuilder concat = new StringBuilder("");
        for (String str : objectAnnotations) {
            concat.append(str);
        }
        return concat.toString();
    }

    public String getObjectImplements() {
        StringBuilder concat = new StringBuilder("");
        int position = 0;
        for (String str : objectImplements) {
            concat.append(str);
            if (position != (objectImplements.size() -1)){
                concat.append(CommonValues.COMA);
            }
            position++;
        }
        return objectImplements.isEmpty() ? "" : CommonValues.SPACE.getValue() + ObjectValues.IMPLEMENTS.getValue() + concat.toString();
    }

    public String getObjectAttributes() {
        StringBuilder concat = new StringBuilder("");
        for (String str : objectAttributes) {
            concat.append(str);
        }
        return !objectAttributes.isEmpty() ? CommonValues.NEWLINE.getValue() + concat.toString() : "";
    }

    public int getObjectAttributesSize() {
        return objectAttributes.size();
    }

    public String getObjectMethods() {
        StringBuilder concat = new StringBuilder("");
        int position = 0;
        for (ObjectMethod method : objectMethods) {
            concat.append(method);
            if (position != (objectMethods.size() -1)){
                concat.append(CommonValues.NEWLINE);
            }
            position++;
        }
        return !objectMethods.isEmpty() ? CommonValues.NEWLINE.getValue() +concat.toString() : "";
    }

    public String getObjectFunctions() {
        StringBuilder concat = new StringBuilder("");
        int position = 0;
        for (ObjectFunction function : objectFunctions) {
            concat.append(function);
            if (position != (objectFunctions.size() -1)){
                concat.append(CommonValues.NEWLINE);
            }
            position++;
        }
        return !objectFunctions.isEmpty() ? CommonValues.NEWLINE.getValue() + concat.toString() : "";
    }

    public String getObjectConstructors() {
        StringBuilder concat = new StringBuilder("");
        int position = 0;
        for (ObjectConstructor constructor : objectConstructors) {
            concat.append(constructor);
            if (position != (objectConstructors.size() -1)){
                concat.append(CommonValues.NEWLINE);
            }
            position++;
        }
        return !objectConstructors.isEmpty() ? CommonValues.NEWLINE.getValue() + concat.toString() : "";
    }

    public String getObjectName() {
        return objectName;
    }

    public String getObjectExtend() {
        return objectExtends.isEmpty() ? "" : CommonValues.SPACE.getValue() + ObjectValues.EXTENDS.getValue() + StringUtils.join(objectExtends, ", ");
    }

    public String getObjectRawBody() {
        return objectRawBody.isEmpty() ? "" : CommonValues.NEWLINE.getValue() + objectRawBody;
    }

    public static class ObjectConstructor {
        private ScopeValues constructorScope;
        private String constructorName;
        private Set<Pair<Object, Object>> constructorArguments = new LinkedHashSet<>();
        private String constructorBody;
        private String constructorAnnotations;

        public ObjectConstructor(ScopeValues constructorScope, String constructorName) {
            this.constructorScope = constructorScope;
            this.constructorName = BuildUtils.cleanSpaces(constructorName);
            this.constructorAnnotations = "";
            this.constructorBody = "";
        }

        public ObjectConstructor addArgument(String argumentClass, String argumentName) {
            constructorArguments.add(new Pair<>(BuildUtils.cleanSpaces(argumentClass), BuildUtils.cleanSpaces(argumentName)));
            return this;
        }

        public ObjectConstructor setBody(String constructorBody) {
            this.constructorBody = constructorBody;
            return this;
        }

        public ObjectConstructor addBodyLine(String constructorBody) {
            this.constructorBody += BuildUtils.buildBodyLine(constructorBody);
            return this;
        }

        public ObjectConstructor addAnnotation(String constructorAnnotation) {
            constructorAnnotations += BuildUtils.buildAnnotation(constructorAnnotation);
            return this;
        }

        public ObjectConstructor addAnnotation(Class<?> clazz) {
            if (clazz != null) {
                this.constructorAnnotations += BuildUtils.buildAnnotation(clazz.getSimpleName());
            }
            return this;
        }

        @Override
        public String toString() {
            return BuildUtils.buildConstructor(constructorAnnotations, constructorScope, constructorName, constructorArguments, constructorBody);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ObjectConstructor that = (ObjectConstructor) o;

            if (constructorScope != that.constructorScope) return false;
            if (constructorName != null ? !constructorName.equals(that.constructorName) : that.constructorName != null)
                return false;
            if (constructorArguments != null ? !constructorArguments.equals(that.constructorArguments) : that.constructorArguments != null)
                return false;
            if (constructorBody != null ? !constructorBody.equals(that.constructorBody) : that.constructorBody != null)
                return false;
            return constructorAnnotations != null ? constructorAnnotations.equals(that.constructorAnnotations) : that.constructorAnnotations == null;
        }

        @Override
        public int hashCode() {
            int result = constructorScope != null ? constructorScope.hashCode() : 0;
            result = 31 * result + (constructorName != null ? constructorName.hashCode() : 0);
            result = 31 * result + (constructorArguments != null ? constructorArguments.hashCode() : 0);
            result = 31 * result + (constructorBody != null ? constructorBody.hashCode() : 0);
            result = 31 * result + (constructorAnnotations != null ? constructorAnnotations.hashCode() : 0);
            return result;
        }
    }

    public static class ObjectFunction {
        private ScopeValues functionScope;
        private String functionName;
        private String functionReturnType;
        private Set<Pair<Object, Object>> functionArguments = new LinkedHashSet<>();
        private String functionBody;
        private String functionAnnotations;
        private String functionReturn;

        public ObjectFunction(ScopeValues functionScope, String functionName, String functionReturnType) {
            this.functionScope = functionScope;
            this.functionName = BuildUtils.cleanSpaces(functionName);
            this.functionReturnType = BuildUtils.cleanSpaces(functionReturnType);
            this.functionBody = "";
            this.functionAnnotations = "";
            this.functionReturn = "";
        }

        public ObjectFunction addArgument(String argumentClass, String argumentName) {
            functionArguments.add(new Pair<>(BuildUtils.cleanSpaces(argumentClass), BuildUtils.cleanSpaces(argumentName)));
            return this;
        }

        public ObjectFunction setBody(String functionBody) {
            this.functionBody = functionBody;
            return this;
        }

        public ObjectFunction addBodyLine(String functionBody) {
            this.functionBody += BuildUtils.buildBodyLine(functionBody);
            return this;
        }

        public ObjectFunction addAnnotation(String functionAnnotation) {
            this.functionAnnotations += BuildUtils.buildAnnotation(functionAnnotation);
            return this;
        }

        public ObjectFunction addAnnotation(Class<?> clazz) {
            if (clazz != null) {
                this.functionAnnotations += BuildUtils.buildAnnotation(clazz.getSimpleName());
            }
            return this;
        }

        public ObjectFunction setFunctionReturn(String functionReturn) {
            this.functionReturn = BuildUtils.cleanSpaces(functionReturn);
            return this;
        }

        @Override
        public String toString() {
            return BuildUtils.buildFunction(functionAnnotations, functionScope, functionReturnType, functionName, functionArguments, functionBody, functionReturn);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ObjectFunction that = (ObjectFunction) o;

            if (functionScope != that.functionScope) return false;
            if (functionName != null ? !functionName.equals(that.functionName) : that.functionName != null)
                return false;
            if (functionReturnType != null ? !functionReturnType.equals(that.functionReturnType) : that.functionReturnType != null)
                return false;
            if (functionArguments != null ? !functionArguments.equals(that.functionArguments) : that.functionArguments != null)
                return false;
            if (functionBody != null ? !functionBody.equals(that.functionBody) : that.functionBody != null)
                return false;
            if (functionAnnotations != null ? !functionAnnotations.equals(that.functionAnnotations) : that.functionAnnotations != null)
                return false;
            return functionReturn != null ? functionReturn.equals(that.functionReturn) : that.functionReturn == null;
        }

        @Override
        public int hashCode() {
            int result = functionScope != null ? functionScope.hashCode() : 0;
            result = 31 * result + (functionName != null ? functionName.hashCode() : 0);
            result = 31 * result + (functionReturnType != null ? functionReturnType.hashCode() : 0);
            result = 31 * result + (functionArguments != null ? functionArguments.hashCode() : 0);
            result = 31 * result + (functionBody != null ? functionBody.hashCode() : 0);
            result = 31 * result + (functionAnnotations != null ? functionAnnotations.hashCode() : 0);
            result = 31 * result + (functionReturn != null ? functionReturn.hashCode() : 0);
            return result;
        }
    }

    public static class ObjectMethod {
        private ScopeValues methodScope;
        private String methodName;
        private Set<Pair<Object, Object>> methodArguments = new LinkedHashSet<>();
        private String methodBody;
        private String methodAnnotations;

        public ObjectMethod(ScopeValues methodScope, String methodName) {
            this.methodScope = methodScope;
            this.methodName = BuildUtils.cleanSpaces(methodName);
            this.methodBody = "";
            this.methodAnnotations = "";
        }

        public ObjectMethod addArgument(String argumentClass, String argumentName) {
            methodArguments.add(new Pair<>(BuildUtils.cleanSpaces(argumentClass), BuildUtils.cleanSpaces(argumentName)));
            return this;
        }

        public ObjectMethod setBody(String methodBody) {
            this.methodBody = methodBody;
            return this;
        }

        public ObjectMethod addBodyLine(String methodBody) {
            this.methodBody += BuildUtils.buildBodyLine(methodBody);
            return this;
        }

        public ObjectMethod addAnnotation(String methodAnnotation) {
            this.methodAnnotations += BuildUtils.buildAnnotation(methodAnnotation);
            return this;
        }

        public ObjectMethod addAnnotation(Class<?> clazz) {
            if (clazz != null) {
                this.methodAnnotations += BuildUtils.buildAnnotation(clazz.getSimpleName());
            }
            return this;
        }

        @Override
        public String toString() {
            return BuildUtils.buildMethod(methodAnnotations, methodScope, methodName, methodArguments, methodBody);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ObjectMethod that = (ObjectMethod) o;

            if (methodScope != that.methodScope) return false;
            if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
            if (methodArguments != null ? !methodArguments.equals(that.methodArguments) : that.methodArguments != null)
                return false;
            if (methodBody != null ? !methodBody.equals(that.methodBody) : that.methodBody != null) return false;
            return methodAnnotations != null ? methodAnnotations.equals(that.methodAnnotations) : that.methodAnnotations == null;
        }

        @Override
        public int hashCode() {
            int result = methodScope != null ? methodScope.hashCode() : 0;
            result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
            result = 31 * result + (methodArguments != null ? methodArguments.hashCode() : 0);
            result = 31 * result + (methodBody != null ? methodBody.hashCode() : 0);
            result = 31 * result + (methodAnnotations != null ? methodAnnotations.hashCode() : 0);
            return result;
        }
    }

}
