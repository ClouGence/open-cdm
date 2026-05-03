package com.clougence.adapter.gauss;

public enum GaussDBParamModeTypes {

    IN("IN"),
    OUT("OUT"),
    INOUT("IN OUT"),
    VARIADIC("VARIADIC");

    private final String codeKey;

    GaussDBParamModeTypes(String codeKey){
        this.codeKey = codeKey;
    }

    public String getCodeKey() { return this.codeKey; }

    public static GaussDBParamModeTypes valueOfCode(String code) {
        code = code.toUpperCase();

        for (GaussDBParamModeTypes modeType : GaussDBParamModeTypes.values()) {
            if (modeType.codeKey.equalsIgnoreCase(code)) {
                return modeType;
            }
        }
        return null;
    }
}
