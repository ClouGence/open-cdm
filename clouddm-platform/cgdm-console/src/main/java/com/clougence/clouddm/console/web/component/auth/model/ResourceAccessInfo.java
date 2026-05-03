package com.clougence.clouddm.console.web.component.auth.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceAccessInfo {

    private boolean     allAllow;
    private Set<String> allowQueryList;

    public ResourceAccessInfo(boolean allAllow){
        this.allAllow = allAllow;
    }

    public ResourceAccessInfo(Set<String> allowQueryList){
        this.allAllow = false;
        this.allowQueryList = allowQueryList;
    }

}
