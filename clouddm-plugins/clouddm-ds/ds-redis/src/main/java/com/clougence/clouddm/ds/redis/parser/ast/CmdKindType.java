package com.clougence.clouddm.ds.redis.parser.ast;

import lombok.Getter;

public enum CmdKindType {

    Read("READ"),
    Write("WRITE"),
    Script("SCRIPT"),
    PubSub("PUBSUB"),
    Monitor("MONITOR"),
    Maintenance("MAINTENANCE"),
    Transaction("TRANSACTION"),
    Connection("CONNECTION"),
    Other("OTHER"),;

    @Getter
    private final String type;

    CmdKindType(String type){
        this.type = type;
    }
}
