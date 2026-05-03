package com.clougence.clouddm.console.web.model.fo.editor.data;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRowFO {

    private Map<String, String> whereData = new HashMap<>();
    private Map<String, String> newData   = new HashMap<>();

    private Integer             sequence;

}
