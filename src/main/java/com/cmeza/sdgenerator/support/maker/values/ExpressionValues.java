package com.cmeza.sdgenerator.support.maker.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cmeza.sdgenerator.support.maker.values.CommonValues.SPACE;

/**
 * Created by carlos on 08/04/17.
 */
@AllArgsConstructor
public enum ExpressionValues {

    EQUAL(SPACE + "=" + SPACE),
    AT("@");

    @Getter
    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
