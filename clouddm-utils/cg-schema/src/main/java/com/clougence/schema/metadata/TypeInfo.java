package com.clougence.schema.metadata;

import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeInfo {

    private String schema;
    private String typeName;
    private String typeCategory;

    public TypeInfo(){
    }

    public TypeInfo(String schema, String typeName){
        this.schema = schema;
        this.typeName = typeName;
    }

    public TypeInfo(String schema, String typeName, String typeCategory){
        this.schema = schema;
        this.typeName = typeName;
        this.typeCategory = typeCategory;
    }

    public String toFullName() {
        return StringUtils.isBlank(schema) ? typeName : (schema + "." + typeName);
    }
}
