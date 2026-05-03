package com.clougence.clouddm.console.web.component.project.model;

import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCheckItemMO {

    private String    specName;
    private String    ruleName;
    private String    ruleDesc;
    //private String    message;
    private WarnLevel level;
    //private Object    result;
}
