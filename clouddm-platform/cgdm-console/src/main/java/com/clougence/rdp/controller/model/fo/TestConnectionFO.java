package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.PgSslMode;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;

import lombok.Data;

/**
 * @author bucketli 2021/1/28 12:22
 */
@Data
public class TestConnectionFO {

    @Min(value = 1, message = "{min.clusterid}")
    private long           clusterId;

    @NotNull(message = "{notnull.dstype}")
    private DataSourceType dsType;

    @NotNull(message = "{notnull.securitytype}")
    private SecurityType   securityType;

    private ConnectType    connectType;

    private String         driver;

    private String         host;

    private String         userName;

    private String         password;

    private String         dbName;

    private Boolean        useSSL;

    private PgSslMode      pgSslMode;

    private boolean        httpsEnabled;
}
