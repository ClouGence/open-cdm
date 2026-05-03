package com.clougence.schema.umi.special.rdb;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.constraint.Primary;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbPrimaryKey extends Primary {

    private List<String> columnList;

    public void setColumnList(List<String> columnList) { this.columnList = columnList; }

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
