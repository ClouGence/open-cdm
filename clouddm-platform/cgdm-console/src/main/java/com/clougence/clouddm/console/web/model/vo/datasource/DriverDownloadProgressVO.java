package com.clougence.clouddm.console.web.model.vo.datasource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverDownloadProgressVO {

    private String  uid;
    private Long    clusterId;
    private String  driverFamily;
    private String  driverVersion;
    private int     totalFileCount;
    private int     completedFileCount;
    private int     currentFilePercent;
    private String  status;
    private String  message;
    private String  resourceCoordinate;
    private String  currentFileName;
    private boolean available;
}
