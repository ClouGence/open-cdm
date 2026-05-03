package com.clougence.schema.umi.special.rdb;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.AttributeUmiData;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbIndex extends AttributeUmiData {

    private String       catalog;
    private String       schema;
    private String       table;
    private String       name;
    private String       comment;
    private RdbIndexType type;
    private List<String> columnList;

    public void addColumn(String column) {
        if (StringUtils.isBlank(column)) {
            return;
        }

        if (this.columnList == null) {
            this.columnList = new ArrayList<>();
        }

        this.columnList.add(column);
    }
}
