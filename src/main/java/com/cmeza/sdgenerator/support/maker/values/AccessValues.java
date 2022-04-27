package com.cmeza.sdgenerator.support.maker.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cmeza.sdgenerator.support.maker.values.CommonValues.SPACE;

/**
 * Created by carlos on 25/04/22.
 */

@AllArgsConstructor
public enum AccessValues {
    FINAL("final" + SPACE),
    STATIC("static" + SPACE);

    @Getter
    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
