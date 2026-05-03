package com.clougence.schema.umi.special.rdb;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.UmiConstraintType;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbForeignKey extends ConstraintObject {

    private String              referenceCatalog;
    private String              referenceSchema;
    private String              referenceTable;
    private Map<String, String> referenceMapping;

    private RdbForeignKeyRule   updateRule;
    private RdbForeignKeyRule   deleteRule;

    private List<String>        columnList;

    public void addColumn(String column, String mappingTo) {
        if (StringUtils.isBlank(column)) {
            return;
        }

        if (this.columnList == null) {
            this.columnList = new ArrayList<>();
        }
        if (this.referenceMapping == null) {
            this.referenceMapping = new LinkedHashMap<>();
        }

        this.columnList.add(column);
        this.referenceMapping.put(column, mappingTo);
    }

    @Override
    public UmiConstraintType getConstraintType() { return GeneralConstraintType.Foreign; }
}
