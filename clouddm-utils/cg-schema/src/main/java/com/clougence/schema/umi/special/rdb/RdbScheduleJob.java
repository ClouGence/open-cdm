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
public class RdbScheduleJob extends AttributeUmiData implements Value {

    private String name;
    private String execSql;
    private String schema;
    private String creator;

    private String enabled;
    private String status;

    private String comment;

    @Override
    public String asValue() {
        return this.name;
    }

    @Override
    public UmiTypes getUmiType() { return UmiTypes.ScheduleJob; }
}
