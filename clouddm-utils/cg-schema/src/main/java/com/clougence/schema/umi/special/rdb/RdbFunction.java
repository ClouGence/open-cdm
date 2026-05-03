package com.clougence.schema.umi.special.rdb;

import java.util.Map;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.RoutineUmiData;
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
public class RdbFunction extends RoutineUmiData implements Value {

    private String              catalog;
    private String              schema;
    private UmiTypes            umiType;
    private RdbParam            returns;
    private String              sql;
    private Map<String, Object> features;

    public RdbFunction(){
        this.umiType = UmiTypes.Function;
    }

    @Override
    public String asValue() {
        return getName();
    }

    public void setReturns(RdbParam param) { this.returns = param; }

    public void setReturn(String param) {
        RdbParam rdbParam = new RdbParam();
        rdbParam.setCatalog(this.catalog);
        rdbParam.setSchema(this.schema);
        rdbParam.setName("RETURN");
        rdbParam.setType(param);
        rdbParam.setMode(RdbParamMode.OUT);
        this.returns = rdbParam;
    }

    public void setReturn(int pOrdinal, String pName, String pType, String pDesc) {
        RdbParam rdbParam = new RdbParam();
        rdbParam.setCatalog(this.catalog);
        rdbParam.setSchema(this.schema);
        rdbParam.setName(pName);
        rdbParam.setType(pType);
        rdbParam.setOrdinal(pOrdinal);
        rdbParam.setMode(RdbParamMode.OUT);
        rdbParam.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, pDesc);
        this.returns = rdbParam;
    }
}
