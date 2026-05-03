package com.clougence.dslparser.detectrule.test.func;

import com.clougence.detectrule.lang.reflect.RuleFunction;
import com.clougence.utils.StringUtils;

public class FuncStringUtils {

    @RuleFunction("isBlank")
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    @RuleFunction("isNotBlank")
    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    @RuleFunction("trim")
    public static String trim(String str, String trimChar) {
        if (StringUtils.isBlank(trimChar)) {
            return StringUtils.trim(str, ' ');
        } else {
            return StringUtils.trim(str, trimChar.charAt(0));
        }
    }

    @RuleFunction("trimEnd")
    public static String trimEnd(String str, String trimChar) {
        if (StringUtils.isBlank(trimChar)) {
            return StringUtils.trimEnd(str, ' ');
        } else {
            return StringUtils.trimEnd(str, trimChar.charAt(0));
        }
    }

    @RuleFunction("trimStart")
    public static String trimStart(String str, String trimChar) {
        if (StringUtils.isBlank(trimChar)) {
            return StringUtils.trimStart(str, ' ');
        } else {
            return StringUtils.trimStart(str, trimChar.charAt(0));
        }
    }

    @RuleFunction("upperCase")
    public static String upperCase(String str) {
        return StringUtils.upperCase(str);
    }

    @RuleFunction("lowerCase")
    public static String lowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    @RuleFunction("hasUpperCase")
    public static boolean hasUpperCase(String str) {
        return StringUtils.hasUpperCase(str);
    }

    @RuleFunction("hasLowerCase")
    public static boolean hasLowerCase(String str) {
        return StringUtils.hasLowerCase(str);
    }

    @RuleFunction("isAllUpperCase")
    public static boolean isAllUpperCase(String str) {
        return StringUtils.isAllUpperCase(str);
    }

    @RuleFunction("contains")
    public static boolean contains(String str, String searchStr) {
        return StringUtils.contains(str, searchStr);
    }

    @RuleFunction("containsIgnoreCase")
    public static boolean containsIgnoreCase(String str, String searchStr) {
        return StringUtils.containsIgnoreCase(str, searchStr);
    }

    @RuleFunction("equals")
    public static boolean equals(String str, String searchStr) {
        return StringUtils.equals(str, searchStr);
    }

    @RuleFunction("equalsIgnoreCase")
    public static boolean equalsIgnoreCase(String str, String searchStr) {
        return StringUtils.equalsIgnoreCase(str, searchStr);
    }

    @RuleFunction("startsWith")
    public static boolean startsWith(String str, String searchStr) {
        return StringUtils.startsWith(str, searchStr);
    }

    @RuleFunction("startsWithIgnoreCase")
    public static boolean startsWithIgnoreCase(String str, String searchStr) {
        return StringUtils.startsWithIgnoreCase(str, searchStr);
    }

    @RuleFunction("endsWith")
    public static boolean endsWith(String str, String searchStr) {
        return StringUtils.endsWith(str, searchStr);
    }

    @RuleFunction("endsWithIgnoreCase")
    public static boolean endsWithIgnoreCase(String str, String searchStr) {
        return StringUtils.endsWithIgnoreCase(str, searchStr);
    }
}
