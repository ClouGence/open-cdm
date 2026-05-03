package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class WriteConcern {

    private Object  w;
    private Integer wtimeout;
    private Boolean j;
}
