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
package com.clougence.clouddm.sec.rules.domain.func;

import java.util.List;

import com.clougence.detectrule.lang.reflect.RuleFunction;
import com.clougence.utils.ArrayUtils;
import com.clougence.utils.StringUtils;

public class FuncStringUtils {

    @RuleFunction("isBlank")
    public boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    @RuleFunction("isNotBlank")
    public boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    @RuleFunction("trim")
    public String trim(String str) {
        return StringUtils.trim(str, ' ');
    }

    @RuleFunction("trimEnd")
    public String trimEnd(String str) {
        return StringUtils.trimEnd(str, ' ');
    }

    @RuleFunction("trimStart")
    public String trimStart(String str) {
        return StringUtils.trimStart(str, ' ');
    }

    @RuleFunction("upperCase")
    public String upperCase(String str) {
        return StringUtils.upperCase(str);
    }

    @RuleFunction("lowerCase")
    public String lowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    @RuleFunction("hasUpperCase")
    public boolean hasUpperCase(String str) {
        return StringUtils.hasUpperCase(str);
    }

    @RuleFunction("hasLowerCase")
    public boolean hasLowerCase(String str) {
        return StringUtils.hasLowerCase(str);
    }

    @RuleFunction("isAllUpperCase")
    public boolean isAllUpperCase(String str) {
        return StringUtils.isAllUpperCase(str);
    }

    @RuleFunction("contains")
    public boolean contains(String str, String searchStr) {
        return StringUtils.contains(str, searchStr);
    }

    @RuleFunction("containsIgnoreCase")
    public boolean containsIgnoreCase(String str, String searchStr) {
        return StringUtils.containsIgnoreCase(str, searchStr);
    }

    @RuleFunction("containsAny")
    public boolean containsAny(String str, List<String> searchStr) {
        for (String s : searchStr) {
            if (StringUtils.contains(str, s)) {
                return true;
            }
        }
        return false;
    }

    @RuleFunction("containsIgnoreCaseAny")
    public boolean containsIgnoreCaseAny(String str, List<String> searchStr) {
        for (String s : searchStr) {
            if (StringUtils.containsIgnoreCase(str, s)) {
                return true;
            }
        }
        return false;
    }

    @RuleFunction("equals")
    public boolean equals(String str, String searchStr) {
        return StringUtils.equals(str, searchStr);
    }

    @RuleFunction("equalsIgnoreCase")
    public boolean equalsIgnoreCase(String str, String searchStr) {
        return StringUtils.equalsIgnoreCase(str, searchStr);
    }

    @RuleFunction("equalsAny")
    public boolean equalsAny(String str, List<String> searchStr) {
        for (String s : searchStr) {
            if (StringUtils.equals(str, s)) {
                return true;
            }
        }
        return false;
    }

    @RuleFunction("equalsIgnoreCaseAny")
    public boolean equalsIgnoreCaseAny(String str, List<String> searchStr) {
        for (String s : searchStr) {
            if (StringUtils.equalsIgnoreCase(str, s)) {
                return true;
            }
        }
        return false;
    }

    @RuleFunction("startsWith")
    public boolean startsWith(String str, String searchStr) {
        return StringUtils.startsWith(str, searchStr);
    }

    @RuleFunction("startsWithIgnoreCase")
    public boolean startsWithIgnoreCase(String str, String searchStr) {
        return StringUtils.startsWithIgnoreCase(str, searchStr);
    }

    @RuleFunction("startsWithAny")
    public boolean startsWithAny(String str, List<String> searchStr) {
        for (String s : searchStr) {
            if (StringUtils.startsWith(str, s)) {
                return true;
            }
        }
        return false;
    }

    @RuleFunction("startsWithIgnoreCaseAny")
    public boolean startsWithIgnoreCaseAny(String str, List<String> searchStr) {
        for (String s : searchStr) {
            if (StringUtils.startsWithIgnoreCase(str, s)) {
                return true;
            }
        }
        return false;
    }

    @RuleFunction("endsWith")
    public boolean endsWith(String str, String searchStr) {
        return StringUtils.endsWith(str, searchStr);
    }

    @RuleFunction("endsWithIgnoreCase")
    public boolean endsWithIgnoreCase(String str, String searchStr) {
        return StringUtils.endsWithIgnoreCase(str, searchStr);
    }

    @RuleFunction("endsWithAny")
    public boolean endsWith(String str, List<String> searchStr) {
        for (String s : searchStr) {
            if (StringUtils.endsWith(str, s)) {
                return true;
            }
        }
        return false;
    }

    @RuleFunction("endsWithIgnoreCaseAny")
    public boolean endsWithIgnoreCase(String str, List<String> searchStr) {
        for (String s : searchStr) {
            if (StringUtils.endsWithIgnoreCase(str, s)) {
                return true;
            }
        }
        return false;
    }

    @RuleFunction("getListElement")
    public String getListElement(List<String> array, long index) {
        return array.get((int) index);
    }

    @RuleFunction("split")
    public String[] split(String str, String separator) {
        if (str == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        } else {
            return StringUtils.split(str, separator);
        }
    }

    @RuleFunction("length")
    public int length(String str) {
        return StringUtils.isBlank(str) ? 0 : str.length();
    }

    @RuleFunction("concat")
    public String concat(List<String> str) {
        return StringUtils.join(str.toArray(), "");
    }

    @RuleFunction("substring")
    public String substring(String str, long start, long end) {
        if (end < 0) {
            return StringUtils.substring(str, (int) start);
        } else {
            return StringUtils.substring(str, (int) start, (int) end);
        }
    }
}
