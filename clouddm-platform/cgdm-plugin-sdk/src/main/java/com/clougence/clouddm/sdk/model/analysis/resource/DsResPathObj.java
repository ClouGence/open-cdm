package com.clougence.clouddm.sdk.model.analysis.resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsResPathObj implements DsResPath {

    private String resPath;

    public DsResPathObj(){
    }

    public DsResPathObj(String resPath){
        this.resPath = resPath;
    }
}
