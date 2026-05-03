package com.clougence.adapter.dameng;

public enum DmParamModeTypes {

    IN("IN"),
    OUT("OUT"),
    INOUT("IN OUT");

    private final String codeKey;

    DmParamModeTypes(String codeKey){
        this.codeKey = codeKey;
    }

    public String getCodeKey() { return this.codeKey; }

    public static DmParamModeTypes valueOfCode(String code) {
        code = code.toUpperCase();

        for (DmParamModeTypes modeType : DmParamModeTypes.values()) {
            if (modeType.codeKey.equalsIgnoreCase(code)) {
                return modeType;
            }
        }
        return null;
    }
}
