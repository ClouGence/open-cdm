package com.clougence.adapter.gauss;


public enum GaussDBForeignMatchOption {

    None("NONE"),
    Full("FULL"),
    Partial("PARTIAL"),;

    private final String typeName;

    GaussDBForeignMatchOption(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static GaussDBForeignMatchOption valueOfCode(String code) {
        for (GaussDBForeignMatchOption foreignKeyRule : GaussDBForeignMatchOption.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported postgres foreignMatchOption " + code);
    }
}
