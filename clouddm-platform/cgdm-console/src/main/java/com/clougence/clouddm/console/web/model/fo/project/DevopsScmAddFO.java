package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.console.web.dal.enumeration.ScmType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevopsScmAddFO {

    private Long    scmId;
    private ScmType scmType;
    private String  display;
    private String  serviceUrl;
    private String  accessToken;
}
