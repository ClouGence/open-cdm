package com.clougence.adapter.hana;

public enum HanaGeoCodeType {

    COUNTRY("COUNTRY"),
    STATE("STATE"),
    COUNTY("COUNTY"),
    CITY("CITY"),
    POSTAL_CODE("POSTAL_CODE"),
    DISTRICT("DISTRICT"),
    STREET("STREET"),
    HOUSE_NUMBER("HOUSE_NUMBER"),
    ADDRESS_LINE("ADDRESS_LINE"),
    GEOCODE("GEOCODE"),;

    private final String typeName;

    HanaGeoCodeType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static HanaGeoCodeType valueOfCode(String code) {
        if (code == null) {
            return null;
        }
        for (HanaGeoCodeType constraintType : HanaGeoCodeType.values()) {
            if (code.contains(constraintType.typeName) || constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }

}
