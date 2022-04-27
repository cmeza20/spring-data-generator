package com.cmeza.sdgenerator.support.maker.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by carlos on 08/04/17.
 */
@AllArgsConstructor
public enum CommonValues {
    NONE(""),
    SPACE(" "),
    NEWLINE("\r\n"),
    TAB("\t"),
    COMA("," + SPACE),
    SEMICOLON(";" + NEWLINE),
    KEY_START(SPACE + "{" + NEWLINE),
    KEY_END("}" + NEWLINE),
    PARENTHESIS("("),
    PARENTHESIS_END(")"),
    COMMENT_START("/**" + NEWLINE),
    COMMENT_BODY("*" + SPACE),
    COMMENT_END("*/" + NEWLINE),
    COMMENT_SINGLE("//" + SPACE),
    DIAMOND_START("<"),
    DIAMOND_END(">");

    @Getter
    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
