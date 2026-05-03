package com.clougence.clouddm.console.web.component.detectrule.domain;

import lombok.Getter;

@Getter
public class SecRangeItem {

    private final String name;
    private final String value;
    private final String desc;

    public SecRangeItem(String name, String value){
        this.name = name;
        this.value = value;
        this.desc = null;
    }

    public SecRangeItem(String name, String value, String desc){
        this.name = name;
        this.value = value;
        this.desc = desc;
    }
}
