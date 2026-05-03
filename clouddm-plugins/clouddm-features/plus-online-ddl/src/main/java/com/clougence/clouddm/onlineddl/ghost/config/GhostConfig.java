package com.clougence.clouddm.sdk.model.onlineddl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GhostConfig {

    private String          sessionId;
    private String          database;
    private String          schema;
    private String          ddlSql;
    private String          fileDir;
    private String          toolHomeDir;
    private String          logFileFullPath;

    /** options **/
    private GhostOptionsDTO ghostOptionsDTO;
}
