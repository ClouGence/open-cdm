package com.clougence.clouddm.console.web.model.vo.project;

import com.clougence.clouddm.console.web.dal.enumeration.ScmType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevopsScmVO {

    private long    scmId;
    private ScmType scmType;
    private String  scmTypeI18n;
    private String  display;
    private String  serviceUrl;
    private boolean enable;
    private String  enableMessage;
}
