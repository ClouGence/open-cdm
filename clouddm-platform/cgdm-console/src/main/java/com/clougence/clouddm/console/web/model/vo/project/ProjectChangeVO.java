package com.clougence.clouddm.console.web.model.vo.project;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectChangeVO {

    private long                    changeId;
    private long                    devopsId;
    private long                    projectId;
    private String                  projectName;
    private ProjectStatus           projectStatus;
    private DmChangeCheckStrategy   flowCheck;
    private DmChangeApproveStrategy flowApprove;
    private DmChangeExecStrategy    flowExecute;

    private long                    scmId;
    private String                  scmDisplay;
    private ScmType                 scmType;
    private String                  scmTypeI18n;

    private String                  repoUrl;
    private String                  repoName;
    private String                  repoBranch;
    private String                  repoScriptPath;

    private long                    dsId;
    private DataSourceType          dsType;
    private String                  dsInstance;
    private String                  dsDesc;
    private String                  dsDisplay;
    private String                  dsHost;
    private List<String>            dsLevels;

    private String                  changeName;
    private String                  changeTime;
    private ProjectChangeStep       currentStep;
    private ProjectChangeStatus     currentStatus;
    private String                  remark;
    private boolean                 locked;

}
