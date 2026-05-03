package com.clougence.clouddm.ds.mongodb.parser.ast;

import lombok.Getter;

public enum CmdKindType {

    READ("READ"),
    WRITE("WRITE"),
    TRANSACTION("TRANSACTION"),
    OTHER("OTHER"),
    DROP("DROP"),
    CREATE("CREATE"),
    MODIFY("MODIFY"),
    ADMIN("ADMIN");

    @Getter
    private final String type;

    CmdKindType(String type){
        this.type = type;
    }
}
