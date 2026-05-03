package com.clougence.clouddm.ds.polardb.definition.porx.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.polar.porx.PolarDbXShardPolicy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2023/5/6 11:06:35
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PorXShardDef {

    private String              schema;

    private String              table;

    private List<String>        dbShardingCols = new ArrayList<>();

    private List<String>        tbShardingCols = new ArrayList<>();

    private PolarDbXShardPolicy dbShardingPolicy;

    private PolarDbXShardPolicy tbShardingPolicy;

    private Integer             tbShardingNum;

    private boolean             broadcast;
}
