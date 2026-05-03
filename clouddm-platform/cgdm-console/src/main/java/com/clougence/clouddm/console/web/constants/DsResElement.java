package com.clougence.clouddm.console.web.constants;

/**
 * @author bucketli 2021/1/6 19:00
 */
public enum DsResElement {

    CATALOG("DS_RES_ELEMENT_DB", "ruleCatalog"),

    SCHEMA("DS_RES_ELEMENT_SCHEMA", "ruleSchema"),

    TABLE("DS_RES_ELEMENT_TABLE", "ruleTable"),

    COLUMN("DS_RES_ELEMENT_COL", "ruleColumn");

    private final String nameI18nKey;

    private final String paramName;

    DsResElement(String i18Key, String paramName){
        this.nameI18nKey = i18Key;
        this.paramName = paramName;
    }

    public String getNameI18nKey() { return nameI18nKey; }

    public String getParamName() { return paramName; }
}
