package com.clougence.clouddm.console.web.model.fo.project;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuideCreateFO {

    @NotBlank(message = "projectName not be blank")
    private String          projectName;
    private String          projectDesc;
    private String          projectOwnerUid;
    private GuidePipelineFO pipeline;
    private GuideImFO       messenger;
    private ProjectOptionFO option;
}
