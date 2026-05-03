package com.clougence.clouddm.console.web.model.vo.project;

import com.clougence.clouddm.console.web.dal.enumeration.DmChangeApproveStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeCheckStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeExecStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectStatus;
import com.clougence.clouddm.console.web.dal.model.DmProjectOption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectVO {

    private long                    projectId;
    private String                  projectCode;
    private ProjectStatus           status;
    private String                  mark;
    private String                  name;
    private String                  desc;
    private String                  projectOwnerName;
    private String                  projectOwnerUID;
    private DmChangeCheckStrategy   flowCheck;
    private DmChangeApproveStrategy flowApprove;
    private DmChangeExecStrategy    flowExecute;
    private DmProjectOption         options;
    private String                  createTime;

}
