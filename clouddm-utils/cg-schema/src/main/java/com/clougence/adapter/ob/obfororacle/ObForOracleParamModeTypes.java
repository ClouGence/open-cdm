package com.clougence.adapter.ob.obfororacle;

public enum ObForOracleParamModeTypes {

    IN("IN"),
    OUT("OUT"),
    INOUT("IN OUT");

    private final String codeKey;

    ObForOracleParamModeTypes(String codeKey){
        this.codeKey = codeKey;
    }

    public String getCodeKey() { return this.codeKey; }

    public static ObForOracleParamModeTypes valueOfCode(String code) {
        code = code.toUpperCase();

        for (ObForOracleParamModeTypes modeType : ObForOracleParamModeTypes.values()) {
            if (modeType.codeKey.equalsIgnoreCase(code)) {
                return modeType;
            }
        }
        return null;
    }
}
