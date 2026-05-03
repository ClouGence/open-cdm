package com.clougence.clouddm.base.metadata.ui.form.config;

import com.clougence.clouddm.base.metadata.ui.form.TypeConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoldTypeConfig implements TypeConfig {

    private boolean defaultExpand = false;

    public FoldTypeConfig(){
    }
}
