package com.clougence.adapter.mysql;

public enum MySQLParamModeTypes {

    IN("IN"),
    OUT("OUT"),
    INOUT("INOUT");

    private final String codeKey;

    MySQLParamModeTypes(String codeKey){
        this.codeKey = codeKey;
    }

    public String getCodeKey() { return this.codeKey; }

    public static MySQLParamModeTypes valueOfCode(String code) {
        code = code.toUpperCase();

        for (MySQLParamModeTypes modeType : MySQLParamModeTypes.values()) {
            if (modeType.codeKey.equalsIgnoreCase(code)) {
                return modeType;
            }
        }
        return null;
    }
}
