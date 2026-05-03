package com.clougence.adapter.hana;

/**
 * @author wanshao
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/6898554c56e249f2aa8747c3dcef3b5d.html
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20d014b575191014b66d8ef88f6a55f6.html
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20d4117e75191014ba5aaab91b3f087d.html
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/cbbbfc20871e4559abfd45a78ad58c02/61eef2834c274690b84cf9847ea30356.html
 * */
public enum HanaIndexType {

    CPBTREE("CPBTREE"),
    BTREE("BTREE"),
    INVERTED_VALUE("INVERTED VALUE"),
    INVERTED_HASH("INVERTED HASH"),
    INVERTED_INDIVIDUAL("INVERTED INDIVIDUAL"),
    FULLTEXT("FULLTEXT"),
    GEOCODE("GEOCODE"),;

    private final String code;

    HanaIndexType(String code){
        this.code = code;
    }

    public static HanaIndexType valueOfCode(String code) {
        if (code == null) {
            return null;
        }
        for (HanaIndexType indexType : HanaIndexType.values()) {
            if (code.contains(indexType.getCode()) || indexType.getCode().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        return null;
    }

    public String getCode() { return code; }
}
