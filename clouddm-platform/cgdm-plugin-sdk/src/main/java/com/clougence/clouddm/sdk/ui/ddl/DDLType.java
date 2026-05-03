package com.clougence.clouddm.sdk.ui.ddl;

import lombok.Getter;

/**
 * @Author: Ekko
 * @Date: 2024-02-27 14:15
 */
@Getter
public enum DDLType {

    Request("Request"),
    Convert("Convert");

    private final String typeName;

    DDLType(String typeName){
        this.typeName = typeName;
    }
}
