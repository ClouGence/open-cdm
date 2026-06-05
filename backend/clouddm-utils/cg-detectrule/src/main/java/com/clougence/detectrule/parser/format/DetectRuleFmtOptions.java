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
