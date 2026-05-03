package com.clougence.rdp.service.openapi.model;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.enumeration.LifeCycleState;

import lombok.Data;

/**
 * @author bucketli 2022/10/25 15:13:21
 */
@Data
public class ApiListDsFO {

    private DataSourceType type;

    private DeployEnvType  deployType;

    private LifeCycleState lifeCycleState;

    private HostType       hostType;

    private Long           dataSourceId;

    private String         dataSourceDescLike;

    private String         dsHostLike;

    private String         instanceIdLike;

    private String         uid;
}
