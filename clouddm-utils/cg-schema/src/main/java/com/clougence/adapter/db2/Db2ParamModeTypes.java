package com.clougence.adapter.db2;

public enum Db2ParamModeTypes {

    IN("IN"),
    OUT("OUT"),
    INOUT("INOUT");

    private final String codeKey;

    Db2ParamModeTypes(String codeKey){
        this.codeKey = codeKey;
    }

    public String getCodeKey() { return this.codeKey; }

    public static Db2ParamModeTypes valueOfCode(String code) {
        code = code.toUpperCase();

        for (Db2ParamModeTypes modeType : Db2ParamModeTypes.values()) {
            if (modeType.codeKey.equalsIgnoreCase(code)) {
                return modeType;
            }
        }
        return null;
    }
}
