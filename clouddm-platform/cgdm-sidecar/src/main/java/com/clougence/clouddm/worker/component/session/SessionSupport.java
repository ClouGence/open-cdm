package com.clougence.clouddm.worker.component.session;

import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.service.file.FileService;
import com.clougence.clouddm.worker.component.notify.SidecarSqlNotifyService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionSupport {

    private String                  sessionId;
    private String                  localWsn;

    private FileService             fileService;
    private SidecarSqlNotifyService notifyService;
    private ValueProcessService     resultProcessSpi;
}
