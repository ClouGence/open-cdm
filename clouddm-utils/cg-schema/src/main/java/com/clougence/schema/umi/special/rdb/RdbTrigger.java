package com.clougence.schema.umi.special.rdb;

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.struts.RoutineUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbTrigger extends RoutineUmiData implements Value {

    private List<String>        triggerEvent;
    private String              triggerTableCatalog;
    private String              triggerTableSchema;
    private String              triggerTableName;
    private List<String>        triggerTableColumns;
    private String              triggerTime;

    private String              sql;
    private Map<String, Object> features;

    private UmiTypes            umiType;

    public RdbTrigger(){
        this.umiType = UmiTypes.Trigger;
    }

    @Override
    public String asValue() {
        return this.getName();
    }
}
