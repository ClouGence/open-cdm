package com.clougence.clouddm.api.console.sqlaudit;

import java.util.Date;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.Requester;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlExecNotifyDTO {

    private Type         type;

    private SqlStatus    sqlStatus;
    private String       sql;
    private String       uid;
    private String       clientIp;
    private String       sessionId;
    private String       queryId;
    private Requester    requester;
    private long         line;
    private Long         dsId;
    private List<String> levels;

    private String       message;
    private Date         time;

    private boolean      isExplain;

    private boolean      rewrite;
    private List<String> rewriteTag;
    private String       originalSql;
}
