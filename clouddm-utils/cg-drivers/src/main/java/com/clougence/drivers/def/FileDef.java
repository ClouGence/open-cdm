package com.clougence.drivers.def;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDef {

    private String  relativePath;
    private String  absolutePath;
    private boolean prepared;
}
