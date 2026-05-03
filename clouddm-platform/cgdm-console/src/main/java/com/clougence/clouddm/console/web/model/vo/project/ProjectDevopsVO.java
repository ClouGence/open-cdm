package com.clougence.clouddm.console.web.model.vo.project;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.ScmType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDevopsVO {

    private long           devopsId;

    private long           scmId;
    private String         scmDisplay;
    private ScmType        scmType;
    private String         scmTypeI18n;

    private String         repoUrl;
    private String         repoName;
    private String         repoBranch;
    private String         repoScriptPath;

    private long           dsId;
    private DataSourceType dsType;
    private String         dsInstance;
    private String         dsDesc;
    private String         dsHost;
    private List<String>   dsLevels;

    private String         webHookUrl;
    private String         webHookPwd;
    private String         webHookHelpUrl;
    private boolean        webHookEnable;

    private String         callbackUrl;
    private String         callbackMethod;
    private boolean        callbackEnable;

    private String         triggerUrl;
    private String         triggerToken;
    private boolean        triggerEnable;

    private boolean        enable;
}
