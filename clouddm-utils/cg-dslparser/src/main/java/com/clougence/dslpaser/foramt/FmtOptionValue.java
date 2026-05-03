package com.clougence.dslpaser.foramt;

/**
 * format or toString print
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
public enum FmtOptionValue {

    BOOLEAN_SET("True", "False"),
    LINE_SEPARATOR_SET("CRLF", "LF"),
    INTEGER(),
    ELEMENT_AS_NEW_LINE("Inline", "Expression", "Any"),
    PAIR_AS_NEW_LINE("Inline", "Pair"),
    ELEMENT_AS_SPACE("No", "Before", "Around", "After")

    ;

    FmtOptionValue(String... keys){
    }
}
