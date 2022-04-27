package com.cmeza.sdgenerator.support.maker.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cmeza.sdgenerator.support.maker.values.CommonValues.SPACE;

/**
 * Created by carlos on 08/04/17.
 */
@AllArgsConstructor
public enum ObjectValues {
    PACKAGE("package" + SPACE),
    IMPORT("import" + SPACE),
    IMPLEMENTS("implements" + SPACE),
    THIS("this."),
    EXTENDS("extends" + SPACE),
    RETURN("return" + SPACE);

    @Getter
    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
