package com.clougence.clouddm.sec.rules.domain.func;

import java.util.ArrayList;
import java.util.List;

import com.clougence.detectrule.lang.reflect.RuleFunction;

public class FuncLoggerUtils {

    public static final List<String> outputLog = new ArrayList<>();

    @RuleFunction("print")
    public String print(Object str) {
        outputLog.add(String.valueOf(str));
        return str.toString();
    }
}
