package com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnTypeDomain;

import lombok.Getter;

@Getter
public class PgColumnTypeDomain extends ColumnTypeDomain {

    private boolean array;

    public PgColumnTypeDomain(String type, String fullName, String length){
        super(type, fullName, length);
    }

    public PgColumnTypeDomain(String type, String fullName, String length, boolean array){
        super(type, fullName, length);
        this.array = array;
    }
}
