package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.rdp.dal.enumeration.*;

import lombok.Data;

/**
 * @author wanshao create time is 2019/12/11 10:11 下午 finished
 **/
@Data
@TableName(value = "rdp_data_source_history")
public class RdpDataSourceHistoryDO {

    @TableId(type = IdType.AUTO)
    private Long                   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtModified;

    private String                 uid;

    private String                 owner;

    private DeployEnvType          deployType;

    private DeployEnvInfoFetchType infoFetchType;

    private DataSourceType         dataSourceType;

    /**
     * ip,port
     */
    private String                 host;

    private String                 privateHost;

    private String                 publicHost;

    private HostType               hostType;

    private String                 instanceDesc;

    private String                 version;

    private String                 instanceId;

    /**
     * if security type is Kerberos, account content is principal. if security type is None ,account content maybe is Hive hadoop user and other. if security type is USER_PASSWORD,account content is
     * username.
     */
    private String                 account;

    private String                 password;

    private String                 accessKey;

    private String                 secretKey;

    private LifeCycleState         lifeCycleState;

    private String                 securityFileUrl;

    private SecurityType           securityType;

    private SecurityType           publicSecurityType;

    private SecurityFileStoreType  securityFileStoreType;

    private long                   consoleJobId;

    public void mapperWithoutGmtPwd(RdpDataSourceDO dsDO) {
        this.id = dsDO.getId();
        this.uid = dsDO.getUid();
        this.owner = dsDO.getOwner();
        this.deployType = dsDO.getDeployType();
        this.infoFetchType = dsDO.getInfoFetchType();
        this.dataSourceType = dsDO.getDataSourceType();
        this.host = dsDO.getHost();
        this.privateHost = dsDO.getPrivateHost();
        this.publicHost = dsDO.getPublicHost();
        this.hostType = dsDO.getHostType();
        this.instanceDesc = dsDO.getInstanceDesc();
        this.version = dsDO.getVersion();
        this.instanceId = dsDO.getInstanceId();
        this.lifeCycleState = dsDO.getLifeCycleState();
        this.securityFileUrl = dsDO.getSecurityFileUrl();
        this.securityType = dsDO.getSecurityType();
        this.securityFileStoreType = dsDO.getSecurityFileStoreType();
        this.consoleJobId = dsDO.getConsoleJobId();
    }
}
