package com.clougence.clouddm.sec.rules.domain.func;

import com.clougence.detectrule.lang.reflect.RuleFunction;
import com.clougence.utils.NumberUtils;

public class FuncNumberUtils {

    @RuleFunction("isNumber")
    public boolean isNumber(String str) {
        return NumberUtils.isNumber(str);
    }
}
