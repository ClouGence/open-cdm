package com.clougence.drivers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverFile {

    private String  absolutePath;
    private String  relativePath;
    private boolean prepared;
}
