package com.clougence.clouddm.console.web.model.vo.project;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.ScmType;
import com.clougence.clouddm.sdk.scm.ScmEventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectScmVO {

    private long                  scmId;
    private ScmType               scmType;
    private String                scmTypeI18n;
    private String                scmDisplay;
    private List<ScmEventType> events;
    private boolean               enable;
    private String                enableMessage;
}
