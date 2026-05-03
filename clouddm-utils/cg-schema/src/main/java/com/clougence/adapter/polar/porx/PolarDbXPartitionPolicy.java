package com.clougence.adapter.polar.porx;

import lombok.Getter;

/**
 * @author bucketli 2020/12/25 18:19
 */
public enum PolarDbXPartitionPolicy {

    KEY("KEY"),

    HASH("HASH"),

    RANGE_COLUMNS("RANGE COLUMNS"),

    RANGE("RANGE"),

    LIST_COLUMNS("LIST COLUMNS"),

    LIST("LIST");

    @Getter
    private String expr;

    PolarDbXPartitionPolicy(String expr){
        this.expr = expr;
    }
}
