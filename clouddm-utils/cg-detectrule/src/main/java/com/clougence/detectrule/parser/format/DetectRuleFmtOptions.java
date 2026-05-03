package com.clougence.detectrule.parser.format;

import com.clougence.dslpaser.foramt.FmtOption;
import com.clougence.dslpaser.foramt.FmtOptionValue;
import com.clougence.dslpaser.foramt.FmtOptions;

/**
 * format or toString print
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
public interface DetectRuleFmtOptions extends FmtOptions {

    FmtOption NEW_LINE_IF_AROUND_CONDITION              = FmtOption.of("new_line_if_around_condition",//
            FmtOptionValue.BOOLEAN_SET, "True");
    FmtOption NEW_LINE_DEFINE_ENUMS_ELEMENT_EMPTY       = FmtOption.of("new_line_define_enums_element_empty",//
            FmtOptionValue.BOOLEAN_SET, "False");

    FmtOption SPACES_DEFINE_ENUMS_ELEMENT_EMPTY         = FmtOption.of("spaces_define_enums_element_empty",//
            FmtOptionValue.BOOLEAN_SET, "False");

    FmtOption WRAPPING_DEFINE_ENUMS_ELEMENT_AS_NEW_LINE = FmtOption.of("wrapping_define_enums_element_as_new_line",//
            FmtOptionValue.ELEMENT_AS_NEW_LINE, "Inline");
}
