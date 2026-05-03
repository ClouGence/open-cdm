package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import com.clougence.drivers.adapter.AdapterRequest;

import lombok.Getter;

@Getter
public class MongoRequest extends AdapterRequest {

    private final String commandBody;

    public MongoRequest(String commandBody){
        this.commandBody = commandBody;
    }
}
