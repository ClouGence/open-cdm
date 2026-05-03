package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.clouddm.console.web.constants.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.dal.handler.EnumOfCode;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
public enum SecRangeType implements EnumOfCode<SecRangeType> {

    Environment(I18nDmLabelKeys.RDB_ENV),
    Instance(I18nDmLabelKeys.RDB_INSTANCE),
    Catalog(I18nDmLabelKeys.RDB_CATALOG),
    Schema(I18nDmLabelKeys.RDB_SCHEMA),
    TableOrView(I18nDmLabelKeys.RDB_TABLE_OR_VIEW),
    Column(I18nDmLabelKeys.RDB_COLUMN);

    private final String i18nKey;

    SecRangeType(I18nDmLabelKeys i18nKey){
        this.i18nKey = i18nKey.name();
    }

    public static SecRangeType ofTarget(TargetType type) {
        switch (type) {
            case Environment:
                return Environment;
            case Instance:
                return Instance;
            case Catalog:
                return Catalog;
            case Schema:
                return Schema;
            case Table:
            case View:
            case Materialized:
                return TableOrView;
            case Column:
                return Column;
            default:
                return null;
        }
    }

    @Override
    public SecRangeType valueOfCode(String codeString) {
        try {
            return SecRangeType.valueOf(codeString);
        } catch (Exception e) {
            return ofTarget(TargetType.valueOf(codeString));
        }
    }
}
