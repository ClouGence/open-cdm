package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class LatencyStatsFunc extends CollectionFunc {

    private String option;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.LATENCY_STATS; }

    @Override
    public String toBson() {
        return "{\"aggregate\":" + getQuoteName() + ",\"pipeline\":[{$collStats: { latencyStats: " + option + "}}],\"cursor\":{}}";
    }
}
