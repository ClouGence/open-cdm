package com.clougence.adapter.gauss;


public enum GaussDBConstraintType {

    PrimaryKey("PRIMARY KEY"),
    Unique("UNIQUE"),
    ForeignKey("FOREIGN KEY"),
    //    Check("CHECK"),
    ;

    private final String typeName;

    GaussDBConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static GaussDBConstraintType valueOfCode(String code) {
        for (GaussDBConstraintType constraintType : GaussDBConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        throw new UnsupportedOperationException("Unsupported postgres constraintType " + code);
    }
}
