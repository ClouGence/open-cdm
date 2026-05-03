package com.clougence.rdp.service.enumeration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/5/21
 **/
@Getter
@Setter
public class SchemaUnit {

    private String catalog;
    private String schema;

    public SchemaUnit(){
    }

    public SchemaUnit(String catalog, String schema){
        this.catalog = catalog;
        this.schema = schema;
    }
}
