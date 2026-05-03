package com.clougence.schema.editor.domain;

import java.util.*;

import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/5/21 19:56
 */
@Getter
@Setter
public class EForeignKey extends EChange<EForeignKey> {

    private String              name;
    private List<String>        columnList       = new ArrayList<>();
    private String              referenceSchema;
    private String              referenceTable;
    private Map<String, String> referenceMapping = new HashMap<>();
    private RdbForeignKeyRule   updateRule       = RdbForeignKeyRule.NoAction;
    private RdbForeignKeyRule   deleteRule       = RdbForeignKeyRule.NoAction;
    private Map<String, String> attribute        = new HashMap<>();

    @Override
    public EForeignKey clone() {
        EForeignKey eForeignKey = new EForeignKey();
        eForeignKey.name = this.name;
        eForeignKey.columnList.addAll(this.columnList);

        eForeignKey.referenceSchema = this.referenceSchema;
        eForeignKey.referenceTable = this.referenceTable;

        eForeignKey.referenceMapping.putAll(this.referenceMapping);

        eForeignKey.updateRule = this.updateRule;
        eForeignKey.deleteRule = this.deleteRule;

        eForeignKey.attribute.putAll(this.attribute);
        return eForeignKey;
    }

    public boolean testChanged(EForeignKey initData) {
        if (initData == null) {
            return true;
        }
        if (!StringUtils.equals(this.name, initData.name)) {
            return true;
        }
        if (!Objects.deepEquals(this.columnList, initData.columnList)) {
            return true;
        }
        if (!Objects.deepEquals(this.referenceSchema, initData.referenceSchema)) {
            return true;
        }
        if (!Objects.equals(this.referenceTable, initData.referenceTable)) {
            return true;
        }
        if (!Objects.equals(this.updateRule, initData.updateRule)) {
            return true;
        }
        if (!Objects.equals(this.deleteRule, initData.deleteRule)) {
            return true;
        }
        if (EditorUtils.testAttribute(this.referenceMapping, initData.referenceMapping)) {
            return true;
        }
        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        return false;
    }
}
