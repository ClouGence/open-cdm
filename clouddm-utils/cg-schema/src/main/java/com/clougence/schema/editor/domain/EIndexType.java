package com.clougence.schema.editor.domain;

/**
 * @author mode 2021/5/21 19:56
 */
public enum EIndexType {

    Unique("Unique"),
    Normal("Normal"),;

    private final String typeName;

    EIndexType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static EIndexType valueOfCode(String code) {
        for (EIndexType foreignKeyRule : EIndexType.values()) {
            if (foreignKeyRule.typeName.equals(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported indexType " + code);
    }
}
