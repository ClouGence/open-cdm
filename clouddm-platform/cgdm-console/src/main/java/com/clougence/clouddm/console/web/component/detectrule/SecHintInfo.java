package com.clougence.clouddm.console.web.component.detectrule;

import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.RuleLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecHintInfo {

    private List<Integer> lines;
    private String        specName;
    private String        ruleName;
    private String        message;
    private RuleLevel     level;
    private Object        result;
}
