package com.clougence.schema.editor.domain;

import java.util.Objects;

import com.clougence.schema.DsType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/5/21 19:56
 */
@Getter
@Setter
public class EType extends EChange<EType> {

    private String typeName;
    private DsType dsType;

    @Override
    public EType clone() {
        EType eType = new EType();
        eType.typeName = this.typeName;
        eType.dsType = this.dsType;
        return eType;
    }

    @Override
    boolean testChanged(EType initData) {
        if (Objects.equals(this.typeName, initData.typeName)) {
            return true;
        }
        if (Objects.equals(this.dsType, initData.dsType)) {
            return true;
        }
        return false;
    }
}
