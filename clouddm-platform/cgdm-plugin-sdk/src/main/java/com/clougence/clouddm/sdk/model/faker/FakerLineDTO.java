package com.clougence.clouddm.sdk.model.faker;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakerLineDTO {

    private Map<String, String> oldValue;
    private Map<String, String> newValue;

    private List<String>        useWhere;
    private List<String>        useSet;

    private String              type;

    public FakerLineDTO(){

    }

    public FakerLineDTO(Map<String, String> oldValue, Map<String, String> newValue, String type){
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = type;
    }
}
