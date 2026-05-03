package com.clougence.adapter.postgre;

public enum PostgresParamModeTypes {

    IN("IN"),
    OUT("OUT"),
    INOUT("IN OUT"),
    VARIADIC("VARIADIC");

    private final String codeKey;

    PostgresParamModeTypes(String codeKey){
        this.codeKey = codeKey;
    }

    public String getCodeKey() { return this.codeKey; }

    public static PostgresParamModeTypes valueOfCode(String code) {
        code = code.toUpperCase();

        for (PostgresParamModeTypes modeType : PostgresParamModeTypes.values()) {
            if (modeType.codeKey.equalsIgnoreCase(code)) {
                return modeType;
            }
        }
        return null;
    }
}
