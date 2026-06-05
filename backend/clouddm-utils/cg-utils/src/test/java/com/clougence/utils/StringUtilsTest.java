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
package com.clougence.utils;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void trimStart_01() {
        assert StringUtils.trimStart(null, ' ') == null;
        assert StringUtils.trimStart("", ' ').equals("");
        assert StringUtils.trimStart("     ", ' ').equals("");
        assert StringUtils.trimStart("abc", ' ').equals("abc");
        assert StringUtils.trimStart("    abc    ", ' ').equals("abc    ");
        assert StringUtils.trimStart("1111abc1111", '1').equals("abc1111");
    }

    @Test
    public void trimEnd_01() {
        assert StringUtils.trimEnd(null, ' ') == null;
        assert StringUtils.trimEnd("", ' ').equals("");
        assert StringUtils.trimEnd("     ", ' ').equals("");
        assert StringUtils.trimEnd("abc", ' ').equals("abc");
        assert StringUtils.trimEnd("    abc    ", ' ').equals("    abc");
        assert StringUtils.trimEnd("1111abc1111", '1').equals("1111abc");
    }

    @Test
    public void trim_01() {
        assert StringUtils.trim(null, ' ') == null;
        assert StringUtils.trim("", ' ').equals("");
        assert StringUtils.trim("     ", ' ').equals("");
        assert StringUtils.trim("abc", ' ').equals("abc");
        assert StringUtils.trim("1111abc1111", '1').equals("abc");
        assert !StringUtils.trim("2222abc2222", '3').equals("abc");
        assert StringUtils.trim("1111abc1111", '1').equals("abc");
    }

    @Test
    public void join_01() {
        assert StringUtils.join((List<?>) null, ' ') == null;
        assert StringUtils.join(Arrays.asList("a", "b"), ' ').equals("a b");
    }

    @Test
    public void trimBlankStart_01() {
        assert StringUtils.trimBlankStart(null) == null;
        assert StringUtils.trimBlankStart("").equals("");
        assert StringUtils.trimBlankStart("     ").equals("");
        assert StringUtils.trimBlankStart("abc").equals("abc");
        assert StringUtils.trimBlankStart("    abc    ").equals("abc    ");
        assert StringUtils.trimBlankStart("\n\n\nabc\n\n\n").equals("abc\n\n\n");
    }

    @Test
    public void trimBlankEnd_01() {
        assert StringUtils.trimBlankEnd(null) == null;
        assert StringUtils.trimBlankEnd("").equals("");
        assert StringUtils.trimBlankEnd("     ").equals("");
        assert StringUtils.trimBlankEnd("abc").equals("abc");
        assert StringUtils.trimBlankEnd("    abc    ").equals("    abc");
        assert StringUtils.trimBlankEnd("\n\n\nabc\n\n\n").equals("\n\n\nabc");
    }

    @Test
    public void trimBlank_01() {
        assert StringUtils.trimBlank(null) == null;
        assert StringUtils.trimBlank("").equals("");
        assert StringUtils.trimBlank("  \n   ").equals("");
        assert StringUtils.trimBlank("abc").equals("abc");
        assert StringUtils.trimBlank(" \n abc \n ").equals("abc");
        assert StringUtils.trimBlank("1 \n abc \n ").equals("1 \n abc");
    }
}
