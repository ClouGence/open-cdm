package com.clougence.clouddm.console.web.service.project.domain;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.ScmType;
import com.clougence.clouddm.sdk.scm.ScmEventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmScmDef {

    private ScmType               scmType;
    private String                serviceUrl;
    private boolean               custom;
    private String                helpUrl;
    private List<ScmEventType> events;
}
