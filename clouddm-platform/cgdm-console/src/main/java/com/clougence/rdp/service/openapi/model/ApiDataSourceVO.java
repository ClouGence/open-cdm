package com.clougence.rdp.service.openapi.model;

import java.util.Date;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.rdp.dal.enumeration.ConsoleTaskState;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.enumeration.LifeCycleState;
import com.clougence.rdp.dal.model.RdpDataSourceDO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/10/12 10:01
 */
@Getter
@Setter
public class ApiDataSourceVO {

    private boolean          hasPassword;

    private Long             id;

    private Date             gmtCreate;

    private Date             gmtModified;

    private String           uid;

    private String           owner;

    private DeployEnvType    deployType;

    private DataSourceType   dataSourceType;

    private String           privateHost;

    private String           publicHost;

    private HostType         hostType;

    private String           instanceDesc;

    private String           version;

    private String           instanceId;

    private String           autoCreateAccount;

    private String           schemaJson;

    private long             consoleJobId;

    private ConsoleTaskState consoleTaskState;

    private String           accountName;

    private LifeCycleState   lifeCycleState;

    private SecurityType     securityType;

    public void convertFromDsVO(RdpDataSourceDO dsDO) {
        this.accountName = dsDO.getAccount();
        // this.autoCreateAccount = dsDO.getAutoCreateAccount();
        this.consoleJobId = dsDO.getConsoleJobId();
        //this.consoleTaskState = dsDO.getConsoleTaskState();
        this.dataSourceType = dsDO.getDataSourceType();
        this.deployType = dsDO.getDeployType();
        this.gmtCreate = dsDO.getGmtCreate();
        this.gmtModified = dsDO.getGmtModified();
        this.privateHost = dsDO.getPrivateHost();
        this.publicHost = dsDO.getPublicHost();
        this.hostType = dsDO.getHostType();
        this.id = dsDO.getId();
        this.instanceDesc = dsDO.getInstanceDesc();
        this.instanceId = dsDO.getInstanceId();
        this.lifeCycleState = dsDO.getLifeCycleState();
        this.owner = dsDO.getOwner();
        this.securityType = dsDO.getSecurityType();
        this.uid = dsDO.getUid();
        this.version = dsDO.getVersion();
    }
}
