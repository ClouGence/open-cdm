package com.clougence.schema.umi.special.rdb;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.AttributeUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbParam extends AttributeUmiData {

    private String       catalog;
    private String       schema;
    private String       name;
    private Integer      ordinal;
    private String       referenceObject;

    private String       type;
    private String       characterMaximumLength;
    private UmiTypes     umiType;

    private Long         length;
    private Integer      numericPrecision;
    private Integer      numericScale;
    private Integer      datetimePrecision;
    private RdbParamMode mode;

    public RdbParam(){
        this.umiType = UmiTypes.Param;
    }
}
