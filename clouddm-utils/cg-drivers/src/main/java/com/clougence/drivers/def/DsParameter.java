package com.clougence.drivers.def;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DsParameter {

    private String       name;
    private String       defaultValue;
    private boolean      require;
    private List<String> options;
    private boolean      optionsOpen;
    private String       description;
}
