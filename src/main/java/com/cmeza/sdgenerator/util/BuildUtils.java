package com.cmeza.sdgenerator.util;

import com.cmeza.sdgenerator.support.maker.values.CommonValues;
import com.cmeza.sdgenerator.support.maker.values.ExpressionValues;
import com.cmeza.sdgenerator.support.maker.values.ObjectValues;
import com.cmeza.sdgenerator.support.maker.values.ScopeValues;
import javafx.util.Pair;

import java.util.Set;

/**
 * Created by carlos on 08/04/17.
 */
public class BuildUtils {

    private BuildUtils() {
    }

    public static String cleanSpaces(String str) {
        return str.replace(" ", "");
    }

    public static String buildPackage(String objectPackage) {
        return ObjectValues.PACKAGE + cleanSpaces(objectPackage) + CommonValues.SEMICOLON;
    }

    public static String buildImport(String objectImport) {
        return ObjectValues.IMPORT + cleanSpaces(objectImport) + CommonValues.SEMICOLON;
    }

    public static String buildAttribute(String attributeClass, String attributeName) {
        return CommonValues.TAB + ScopeValues.PRIVATE.getValue() + cleanSpaces(attributeClass) + CommonValues.SPACE + cleanSpaces(attributeName) + CommonValues.SEMICOLON;
    }

    public static String builDiamond(String objectClass, Object... objects) {

        if (objects != null && objects.length > 0) {
            StringBuilder diamondString = new StringBuilder();
            diamondString.append(cleanSpaces(objectClass));
            diamondString.append(CommonValues.DIAMOND_START);
            for(Object obj: objects) {
                diamondString.append(cleanSpaces(String.valueOf(obj)));
                if (!String.valueOf(objects[objects.length - 1]).equals(String.valueOf(obj))) {
                    diamondString.append(CommonValues.COMA);
                }
            }
            diamondString.append(CommonValues.DIAMOND_END);
            return diamondString.toString();
        }

        return cleanSpaces(objectClass);
    }

    public static String buildAnnotation(String annotation) {
        return (annotation.startsWith(ExpressionValues.AT.getValue()) ? cleanSpaces(annotation) : ExpressionValues.AT + cleanSpaces(annotation)) + CommonValues.NEWLINE;
    }

    public static String buildBodyLine(String bodyLine) {
        return CommonValues.TAB.getValue() + CommonValues.TAB.getValue() + bodyLine + CommonValues.SEMICOLON.getValue();
    }

    public static String buildReturn(String returnString) {
        return CommonValues.TAB.getValue() + CommonValues.TAB.getValue() + ObjectValues.RETURN.getValue() + returnString + CommonValues.SEMICOLON.getValue();
    }

    public static String buildConstructor(String annotations, ScopeValues scope, String objectName, Set<Pair<Object, Object>> arguments, String body) {
        return buildStructure(annotations, scope, null, objectName, arguments, body, null);
    }

    public static String buildFunction(String annotations, ScopeValues scope, String returnStructure, String objectName, Set<Pair<Object, Object>> arguments, String body, String returnString) {
        return buildStructure(annotations, scope, returnStructure, objectName, arguments, body, returnString);
    }

    public static String buildMethod(String annotations, ScopeValues scope, String objectName, Set<Pair<Object, Object>> arguments, String body) {
        return buildStructure(annotations, scope, "void", objectName, arguments, body, null);
    }

    public static String buildStructure(String annotations, ScopeValues scope, String returnStructure, String objectName, Set<Pair<Object, Object>> arguments, String body, String returnString) {
        StringBuilder constructor = new StringBuilder();
        if (annotations != null && !annotations.isEmpty()) {
            constructor.append(CommonValues.TAB.getValue());
            constructor.append(annotations);
        }

        constructor.append(CommonValues.TAB)
                .append(scope)
                .append(returnStructure == null ? "" : returnStructure + CommonValues.SPACE)
                .append(objectName)
                .append(CommonValues.PARENTHESIS);


        if (arguments != null && !arguments.isEmpty()) {
            int position = 0;
            for (Pair<Object, Object> obj : arguments) {
                constructor.append(obj.getKey())
                        .append(CommonValues.SPACE)
                        .append(obj.getValue());
                if (position != (arguments.size() -1)) {
                    constructor.append(CommonValues.COMA);
                }
                position++;
            }

        }

        constructor.append(CommonValues.PARENTHESIS_END)
                .append(CommonValues.KEY_START)
                .append(body.isEmpty() ? CommonValues.NEWLINE : body)
                .append(returnString == null ? "" : returnString.isEmpty() ? "" : buildReturn(returnString))
                .append(CommonValues.TAB)
                .append(CommonValues.KEY_END);

        return constructor.toString();
    }

}
