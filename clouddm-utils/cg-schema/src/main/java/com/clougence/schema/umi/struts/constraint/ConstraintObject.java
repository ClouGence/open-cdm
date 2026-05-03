package com.clougence.schema.umi.struts.constraint;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.AttributeUmiData;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiConstraintType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class ConstraintObject extends AttributeUmiData implements UmiConstraint {

    private String  catalog;
    private String  schema;
    private String  table;
    private String  name;
    private String  comment;
    private Boolean enabled = true;

    @Override
    public UmiConstraintType getConstraintType() { return () -> "Normal"; }
}
