package com.clougence.clouddm.sec.rules.domain.func;

import com.clougence.detectrule.lang.reflect.RuleFunction;

import java.util.HashMap;
import java.util.Map;

public class FuncOutParamUtils {

    private static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();

    public static Map<String, String> getOutParams() { return threadLocal.get(); }

    public static void initOutParams() {
        threadLocal.set(new HashMap<>());
    }

    public static void clearOutParams() {
        threadLocal.remove();
    }

    @RuleFunction("putParam")
    public void putParam(String key, String value) {
        getOutParams().put(key, value);
    }
}
