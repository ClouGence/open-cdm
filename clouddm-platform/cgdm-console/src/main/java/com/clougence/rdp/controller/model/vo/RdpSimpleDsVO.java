package com.clougence.rdp.controller.model.vo;

import java.util.Date;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.rdp.dal.enumeration.DeployEnvInfoFetchType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.enumeration.LifeCycleState;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.util.RdpI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/2/22 11:07
 */
@Getter
@Setter
public class RdpSimpleDsVO {

    private Long                   id;

    private Date                   gmtCreate;

    private DeployEnvType          deployType;

    private String                 deployTypeI18n;

    private DeployEnvInfoFetchType infoFetchType;

    private DataSourceType         dataSourceType;

    private String                 host;

    private String                 privateHost;

    private String                 publicHost;

    private HostType               hostType;

    private String                 instanceDesc;

    private String                 version;

    private String                 instanceId;

    private String                 accountName;

    private LifeCycleState         lifeCycleState;

    private SecurityType           securityType;

    private Long                   dsEnvId;

    private String                 dsEnvName;

    public void convertFromDO(RdpDataSourceDO dsDO) {
        this.id = dsDO.getId();
        this.gmtCreate = dsDO.getGmtCreate();
        this.host = dsDO.getHost();
        this.privateHost = dsDO.getPrivateHost();
        this.publicHost = dsDO.getPublicHost();
        this.hostType = dsDO.getHostType();
        this.accountName = dsDO.getAccount();
        this.lifeCycleState = dsDO.getLifeCycleState();
        this.securityType = dsDO.getSecurityType();
        this.dsEnvId = dsDO.getDsEnvId();
        if (dsDO.getDsEnvDO() != null) {
            this.dsEnvName = dsDO.getDsEnvDO().getEnvName();
        }
        this.instanceId = dsDO.getInstanceId();
        this.instanceDesc = dsDO.getInstanceDesc();
        this.dataSourceType = dsDO.getDataSourceType();
        if (dsDO.getDeployType() != null) {
            this.deployType = dsDO.getDeployType();
            this.deployTypeI18n = RdpI18nUtils.getMessage(dsDO.getDeployType().name());
        }

        this.infoFetchType = dsDO.getInfoFetchType();

        this.version = dsDO.getVersion();
    }
}
