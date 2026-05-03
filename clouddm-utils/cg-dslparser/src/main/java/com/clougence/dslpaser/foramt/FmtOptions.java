package com.clougence.dslpaser.foramt;

import com.clougence.utils.SystemUtils;

/**
 * format or toString print
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
public interface FmtOptions {

    FmtOption FILE_ENCODE_LINE_SEPARATOR         = FmtOption.of("file_encode_line_separator",//
            FmtOptionValue.LINE_SEPARATOR_SET, SystemUtils.isWindows() ? "CRLF" : "LF");

    FmtOption TABS_AND_INDENTS_USE_TAB_CHAR      = FmtOption.of("tabs_and_indents_use_tab_char",//
            FmtOptionValue.BOOLEAN_SET, "False");
    FmtOption TABS_AND_INDENTS_INDENTS_SIZE      = FmtOption.of("tabs_and_indents_indents_size",//
            FmtOptionValue.INTEGER, "4");

    FmtOption WRAPPING_ARRAY_ELEMENT_AS_NEW_LINE = FmtOption.of("wrapping_array_element_as_new_line",//
            FmtOptionValue.ELEMENT_AS_NEW_LINE, "Inline");
    FmtOption WRAPPING_PAIR_ELEMENT_AS_NEW_LINE  = FmtOption.of("wrapping_pair_element_as_new_line",//
            FmtOptionValue.PAIR_AS_NEW_LINE, "Inline");
    FmtOption WRAPPING_PAIR_ELEMENT_AS_SPACE     = FmtOption.of("wrapping_pair_element_as_space",//
            FmtOptionValue.ELEMENT_AS_SPACE, "After");
    FmtOption WRAPPING_WRAP_SYMBOL               = FmtOption.of("wrapping_wrap_symbol",//
            FmtOptionValue.ELEMENT_AS_SPACE, "Around");

    FmtOption SPACES_BEFORE_KEYWORDS_TYPE_CAST   = FmtOption.of("spaces_before_keywords_in_cast",//
            FmtOptionValue.BOOLEAN_SET, "True");
    FmtOption SPACES_EMPTY_ARRAY                 = FmtOption.of("spaces_empty_array",//
            FmtOptionValue.BOOLEAN_SET, "False");
    FmtOption SPACES_EMPTY_OBJECT                = FmtOption.of("spaces_empty_object",//
            FmtOptionValue.BOOLEAN_SET, "False");

    FmtOption NEW_LINE_EMPTY_ARRAY               = FmtOption.of("new_line_empty_array",//
            FmtOptionValue.BOOLEAN_SET, "False");
    FmtOption NEW_LINE_EMPTY_OBJECT              = FmtOption.of("new_line_empty_object",//
            FmtOptionValue.BOOLEAN_SET, "False");
}
