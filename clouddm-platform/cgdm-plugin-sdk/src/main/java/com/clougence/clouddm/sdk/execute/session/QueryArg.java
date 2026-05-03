package com.clougence.clouddm.sdk.execute.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryArg implements Cloneable {

    private int     index;
    private String  value;
    private int     jdbcType;
    private String  dataType;
    private boolean isOutParam;

    public QueryArg(){
    }

    public QueryArg(int index, String value, int jdbcType, String dataType, boolean isOutParam){
        this.index = index;
        this.value = value;
        this.jdbcType = jdbcType;
        this.dataType = dataType;
        this.isOutParam = isOutParam;
    }

    @Override
    public QueryArg clone() {
        QueryArg arg = new QueryArg();
        arg.index = this.index;
        arg.value = this.value;
        arg.jdbcType = this.jdbcType;
        arg.dataType = this.dataType;
        arg.isOutParam = this.isOutParam;
        return arg;
    }

    @Override
    public String toString() {
        return this.value + "[" + this.dataType + "]";
    }
}
