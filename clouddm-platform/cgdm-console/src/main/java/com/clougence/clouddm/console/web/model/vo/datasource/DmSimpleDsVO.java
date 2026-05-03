package com.clougence.clouddm.console.web.model.vo.datasource;

import java.util.Date;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.enumeration.LifeCycleState;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/2/22 11:07
 */
@Getter
@Setter
public class DmSimpleDsVO {

    private Long           id;
    private Date           gmtCreate;
    private DeployEnvType  deployType;
    private String         deployTypeI18n;
    private DataSourceType dataSourceType;
    private String         host;
    private String         privateHost;
    private String         publicHost;
    private HostType       hostType;
    private String         instanceDesc;
    private String         version;
    private String         instanceId;
    private String         accountName;
    private LifeCycleState lifeCycleState;
    private SecurityType   securityType;
    private Long           dsEnvId;
    private String         dsEnvName;
    private boolean        enableQuery;
    private boolean        enableDevops;
}
