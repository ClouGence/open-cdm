package com.clougence.clouddm.base.metadata.ui.form.config;

import com.clougence.clouddm.base.metadata.ui.form.TypeConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionDefineTypeConfig implements TypeConfig {

    // if maxLevel ,There is no limit to the number of layers
    private Integer maxLevel;

    public PartitionDefineTypeConfig(Integer maxLevel){
        this.maxLevel = maxLevel;
    }

    public PartitionDefineTypeConfig(){
    }
}
