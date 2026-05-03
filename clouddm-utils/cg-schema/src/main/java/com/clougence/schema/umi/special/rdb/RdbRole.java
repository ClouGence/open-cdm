package com.clougence.schema.umi.special.rdb;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.AttributeUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbRole extends AttributeUmiData implements Value {

    private String roleName;

    @Override
    public String asValue() {
        return roleName;
    }

    @Override
    public UmiTypes getUmiType() { return UmiTypes.ROLE; }
}
