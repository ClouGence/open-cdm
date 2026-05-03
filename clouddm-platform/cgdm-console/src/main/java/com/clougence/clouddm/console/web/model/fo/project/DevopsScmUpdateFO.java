package com.clougence.clouddm.console.web.model.fo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevopsScmUpdateFO {

    private long    scmId;
    private String  newDisplay;
    private String  newServiceUrl;
    private String  newAccessToken;
    private boolean force;
}
