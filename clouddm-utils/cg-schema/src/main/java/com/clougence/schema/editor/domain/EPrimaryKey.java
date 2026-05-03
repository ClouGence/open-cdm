package com.clougence.schema.editor.domain;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/5/21 19:56
 */
@Getter
@Setter
public class EPrimaryKey extends EChange<EPrimaryKey> {

    private String              primaryKeyName;
    private List<String>        columnList = new ArrayList<>();
    private Map<String, String> attribute  = new HashMap<>();

    @Override
    public EPrimaryKey clone() {
        EPrimaryKey ePrimaryKey = new EPrimaryKey();

        ePrimaryKey.primaryKeyName = primaryKeyName;
        ePrimaryKey.columnList.addAll(this.columnList);
        ePrimaryKey.attribute.putAll(this.attribute);
        return ePrimaryKey;
    }

    @Override
    public boolean testChanged(EPrimaryKey initData) {
        if (initData == null) {
            return true;
        }
        if (!Objects.equals(this.primaryKeyName, initData.primaryKeyName)) {
            return true;
        }
        if (!Objects.deepEquals(this.columnList, initData.columnList)) {
            return true;
        }
        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        return false;
    }
}
