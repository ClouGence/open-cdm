package com.clougence.schema.editor.domain;

import com.clougence.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class EConstraint extends EChange<EConstraint> {

    private String              name;
    private Map<String, String> attribute  = new HashMap<>();
    private EConstraintType     type;
    private String              expression;
    private List<String>        columnList = new ArrayList<>();

    //
    private Boolean             enabled;

    @Override
    public EConstraint clone() {
        EConstraint eConstraint = new EConstraint();
        eConstraint.name = this.name;
        eConstraint.columnList.addAll(this.columnList);

        eConstraint.type = this.type;
        eConstraint.enabled = this.enabled;
        eConstraint.expression = this.expression;

        eConstraint.attribute.putAll(this.attribute);
        return eConstraint;
    }

    public boolean testChanged(EConstraint initData) {
        if (initData == null) {
            return true;
        }
        if (!StringUtils.equals(this.name, initData.name)) {
            return true;
        }
        if (!Objects.deepEquals(this.columnList, initData.columnList)) {
            return true;
        }

        if (!Objects.equals(this.enabled, initData.enabled)) {
            return true;
        }
        if (!Objects.equals(this.expression, initData.expression)) {
            return true;
        }

        if (this.type != initData.type) {
            return true;
        }

        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        return false;
    }
}
