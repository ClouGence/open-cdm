package com.clougence.clouddm.base.metadata.ui.form.config;

import com.clougence.clouddm.base.metadata.ui.form.TypeConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReferencedColumnTypeConfig implements TypeConfig {

    private String type;

    public ReferencedColumnTypeConfig(String type){
        this.type = type;
    }

    public ReferencedColumnTypeConfig(){
    }

}
