package com.clougence.adapter.hana;

public enum HanaConstraintType {

    /** pk */
    PRIMARY_KEY("PRIMARY KEY"),
    /** uk */
    UNIQUE("UNIQUE"),
    NOT_NULL_UNIQUE("NOT NULL UNIQUE"),
    /** fk */
    FOREIGN_KEY("FOREIGN KEY"),
    /** Specifies a predicate to use as a table check constraint.
     * A table check constraint is satisfied if <search_condition> evaluates to true.
     */
    CHECK_CONSTRAINT("CHECK CONSTRAINT"),

    ;

    private final String typeName;

    HanaConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static HanaConstraintType valueOfCode(String code) {
        for (HanaConstraintType constraintType : HanaConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }

}
