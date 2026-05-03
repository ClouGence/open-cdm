package com.clougence.schema.umi.special.rdb;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.ConstraintUmiData;
import com.clougence.schema.umi.struts.RoutineUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbProcedure extends RoutineUmiData implements Value {

    private String         catalog;
    private String         schema;
    private UmiTypes       umiType;

    private String              sql;
    private Map<String, Object> features;


    public RdbProcedure(){
        this.umiType = UmiTypes.Procedure;
    }

    @Override
    public String asValue() {
        return this.getName();
    }

}
