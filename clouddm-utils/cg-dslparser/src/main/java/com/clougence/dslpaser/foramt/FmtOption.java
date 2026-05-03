package com.clougence.dslpaser.foramt;

import java.util.Map;

/**
 * format or toString print
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
public final class FmtOption {

    private String key;
    private String defaultValue;

    public FmtOption(String key, FmtOptionValue refValueDef, String defaultValue){
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() { return this.key; }

    public boolean getBoolean(Map<String, String> option) {
        if (option == null) {
            return Boolean.parseBoolean(this.defaultValue);
        } else {
            return Boolean.parseBoolean(option.getOrDefault(this.key, this.defaultValue));
        }
    }

    public String getString(Map<String, String> option) {
        if (option == null) {
            return this.defaultValue;
        } else {
            return option.getOrDefault(this.key, this.defaultValue);
        }
    }

    public int getInteger(Map<String, String> option) {
        if (option == null) {
            return Integer.parseInt(this.defaultValue);
        } else {
            return Integer.parseInt(option.getOrDefault(this.key, this.defaultValue));
        }
    }

    public static FmtOption of(String key, FmtOptionValue refValueDef, String defaultValue) {
        return new FmtOption(key, refValueDef, defaultValue);
    }
}
