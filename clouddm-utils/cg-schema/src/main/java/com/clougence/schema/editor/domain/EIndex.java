package com.clougence.schema.editor.domain;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/5/21 19:56
 */
@Getter
@Setter
public class EIndex extends EChange<EIndex> {

    private String              source;
    private String              name;
    private String              comment;
    private EIndexType          type;
    private List<String>        columnList = new ArrayList<>();
    private Map<String, String> attribute  = new HashMap<>();

    public EIndex(String source){
        this.source = source;
    }

    public EIndex(){

    }

    @Override
    public EIndex clone() {
        EIndex eIndex = new EIndex(name);
        eIndex.name = this.name;
        eIndex.comment = this.comment;
        eIndex.type = this.type;
        //
        eIndex.columnList.addAll(this.columnList);
        eIndex.attribute.putAll(this.attribute);
        return eIndex;
    }

    @Override
    public boolean testChanged(EIndex initData) {
        if (initData == null) {
            return true;
        }
        if (!Objects.equals(initData.source, initData.name)) {
            return true;
        }
        if (!Objects.equals(this.name, initData.name)) {
            return true;
        }
        if (!Objects.equals(this.comment, initData.comment)) {
            return true;
        }
        if (this.type != initData.type) {
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

    public boolean testChangedExcept(EIndex initData) {
        if (initData == null) {
            return true;
        }
        if (!Objects.equals(this.name, initData.name)) {
            return true;
        }
        if (!Objects.equals(this.comment, initData.comment)) {
            return true;
        }
        if (this.type != initData.type) {
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
