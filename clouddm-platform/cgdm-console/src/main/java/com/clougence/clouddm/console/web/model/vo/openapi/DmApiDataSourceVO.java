package com.clougence.clouddm.console.web.model.vo.openapi;

import java.util.Date;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmApiDataSourceVO {

    private Long           envId;
    private String         envName;

    private Long           dataSourceId;
    private DataSourceType dataSourceType;

    private String         instanceId;
    private String         instanceDesc;

    private Date           gmtCreate;
    private Date           gmtModified;

    private String         host;
    private String         version;
}
