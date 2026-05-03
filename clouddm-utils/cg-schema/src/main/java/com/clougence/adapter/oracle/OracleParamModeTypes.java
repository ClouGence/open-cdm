package com.clougence.adapter.oracle;

public enum OracleParamModeTypes {

    IN("IN"),
    OUT("OUT"),
    INOUT("IN OUT");

    private final String codeKey;

    OracleParamModeTypes(String codeKey){
        this.codeKey = codeKey;
    }

    public String getCodeKey() { return this.codeKey; }

    public static OracleParamModeTypes valueOfCode(String code) {
        code = code.toUpperCase();

        for (OracleParamModeTypes modeType : OracleParamModeTypes.values()) {
            if (modeType.codeKey.equalsIgnoreCase(code)) {
                return modeType;
            }
        }
        return null;
    }
}
