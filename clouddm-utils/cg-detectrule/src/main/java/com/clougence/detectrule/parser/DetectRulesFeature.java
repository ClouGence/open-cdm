package com.clougence.detectrule.parser;

import lombok.Getter;

@Getter
public enum DetectRulesFeature {

    AllowDefineFeature("ADF"),
    AllowAdvancedTypeFeature("AAT"),
    AllowSymbolAliasFeature("ASA"),
    AllowProgramFeature("AP"),
    AllowUndefinedVariableFeature("AUV");

    private final String sortName;

    DetectRulesFeature(String sortName){
        this.sortName = sortName;
    }

}
