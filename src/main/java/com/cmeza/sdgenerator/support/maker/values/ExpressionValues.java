package com.cmeza.sdgenerator.support.maker.values;

import static com.cmeza.sdgenerator.support.maker.values.CommonValues.SPACE;

/**
 * Created by carlos on 08/04/17.
 */
public enum ExpressionValues {

    EQUAL(SPACE + "=" + SPACE),
    AT("@");

    private String value;

    ExpressionValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
