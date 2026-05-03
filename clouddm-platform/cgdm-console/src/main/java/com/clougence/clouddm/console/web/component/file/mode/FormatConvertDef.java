package com.clougence.clouddm.console.web.component.file.mode;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormatConvertDef {

    private String              name;
    private String              description;
    private String              icon;
    private Map<String, Object> option;
}
