package com.clougence.clouddm.ds.redis.execute.jdbc;

import com.clougence.drivers.adapter.AdapterRequest;

import lombok.Getter;

@Getter
public class JedisRequest extends AdapterRequest {

    private final String commandBody;

    public JedisRequest(String commandBody){
        this.commandBody = commandBody;
    }
}
