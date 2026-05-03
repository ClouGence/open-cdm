package com.clougence.clouddm.sdk.service.secrules;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecResult {

    private boolean             successful;
    private Object              result;
    private List<String>        logger;
    private Map<String, String> outParams;
}
