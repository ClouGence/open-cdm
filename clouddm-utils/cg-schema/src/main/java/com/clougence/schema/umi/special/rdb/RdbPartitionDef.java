package com.clougence.schema.umi.special.rdb;

import java.util.List;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.AttributeUmiData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbPartitionDef extends AttributeUmiData {

    private String                type;
    private String                expression;

    private String                name;
    private String                description;
    private String                tablespace;
    private String                partitionMethod;
    private String                comment;

    private List<RdbPartitionDef> defItems;
}
