package com.clougence.drivers.def;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResDef {

    private String        resourceType;
    private String        coordinate;
    private boolean       prepared;
    private List<FileDef> fileDefList;
}
