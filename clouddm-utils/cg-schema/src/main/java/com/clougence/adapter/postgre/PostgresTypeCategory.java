package com.clougence.adapter.postgre;

import lombok.Getter;

/**
 * @author chunlin create time is 2025/8/15
 * https://www.postgresql.org/docs/17/catalog-pg-type.html
 */
public enum PostgresTypeCategory {

    ArrayTypes("A", PostgresTypes.CHARACTER_VARYING_ARRAY),
    BooleanTypes("B", null),
    CompositeTypes("C", PostgresTypes.CHARACTER_VARYING),
    DateTimeTypes("D", null),
    EnumTypes("E", PostgresTypes.CHARACTER_VARYING),
    GeometricTypes("G", null),
    NetworkAddressTypes("I", null),
    NumericTypes("N", null),
    PseudoTypes("P", null),
    RangeTypes("R", PostgresTypes.CHARACTER_VARYING),
    StringTypes("S", PostgresTypes.CHARACTER_VARYING),
    TimespanTypes("T", null),
    UserDefinedTypes("U", null),
    BitStringTypes("V", null),
    UnknownType("X", null),
    InternalUseTypes("Z", null);

    @Getter
    private final String        code;
    private final PostgresTypes postgresType;

    PostgresTypeCategory(String code, PostgresTypes postgresType){
        this.code = code;
        this.postgresType = postgresType;
    }

    public static PostgresTypeCategory valueOfCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        for (PostgresTypeCategory category : PostgresTypeCategory.values()) {
            if (category.code.equalsIgnoreCase(code)) {
                return category;
            }
        }
        return null;
    }

    public static PostgresTypes getPostgresTypeByCategory(String code) {
        PostgresTypeCategory category = valueOfCode(code);
        if (category != null) {
            return category.postgresType;
        }
        return null;
    }
}
