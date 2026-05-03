package com.clougence.clouddm.sdk.scm;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmEvent {

    private String         hookId;
    private ScmEventType   eventType;
    private Date           eventTime;
    private String         eventId;
    private String         userId;
    private String         userNick;
    private String         userName;
    private String         userEmail;
    private String         tarRepoPath;
    private String         tarRepoName;
    private String         tarRepoBranch;

    private ScmEventTarget target;
    private ScmEventStatus status;

    // source (only PR)
    private String         srcRepoPath;
    private String         srcRepoName;
    private String         srcRepoBranch;

    // other
    private String         title;
    private String         body;

}
