package com.clougence.schema.umi.special.rdb;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.AttributeUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class UmiDsPartition extends AttributeUmiData implements Value {

    private String catalog;
    private String schema;
    private String table;
    private String name;
    private String partitionType;
    private String expression;
    private String description;

    @Override
    public String asValue() {
        return getName();
    }

    @Override
    public UmiTypes getUmiType() { return UmiTypes.PARTITION; }
}
