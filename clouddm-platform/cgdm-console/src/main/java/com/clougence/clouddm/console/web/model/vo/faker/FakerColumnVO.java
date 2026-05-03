package com.clougence.clouddm.console.web.model.vo.faker;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakerColumnVO {

    private Map<String, String> oldValue;
    private Map<String, String> newValue;

    private List<String>        useWhere;
    private List<String>        useSet;

    private String              type;

    public FakerColumnVO(Map<String, String> oldValue, Map<String, String> newValue, List<String> useWhere, List<String> useSet, String type){
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.useWhere = useWhere;
        this.useSet = useSet;
        this.type = type;
    }

}
