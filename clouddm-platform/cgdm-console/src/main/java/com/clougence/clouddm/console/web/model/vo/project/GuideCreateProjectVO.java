package com.clougence.clouddm.console.web.model.vo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuideCreateProjectVO {

    private long   projectId;
    private Long   devopsId;
    private String repoUrl;
    private String webHookUrl;
    private String webHookPwd;
    private String webHookHelpUrl;
}
