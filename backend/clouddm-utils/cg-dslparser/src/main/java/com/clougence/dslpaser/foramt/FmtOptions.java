/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
