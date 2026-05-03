package com.clougence.clouddm.sdk.model.faker;

import com.clougence.utils.StringUtils;

public enum FakerRunModel {

    INCREMENT("INCREMENT"),
    FULL("FULL");

    private final String typeName;

    FakerRunModel(String typeName){
        this.typeName = typeName;
    }

    public static FakerRunModel valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (FakerRunModel type : FakerRunModel.values()) {
            if (StringUtils.equalsIgnoreCase(type.typeName, code)) {
                return type;
            }
        }
        return null;
    }
}
