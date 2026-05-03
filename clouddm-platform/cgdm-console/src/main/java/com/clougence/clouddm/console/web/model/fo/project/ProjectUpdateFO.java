package com.clougence.clouddm.console.web.model.fo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectUpdateFO {

    private long   projectId;

    private String newAdminUid;
    private String newName;
    private String newDesc;
    private String newMark;
}
