package com.clougence.rdp.dal.model.queryobj;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.dal.enumeration.LifeCycleState;

import lombok.Builder;
import lombok.Data;

/**
 * @author wanshao create time is 2021/12/13
 **/
@Data
@Builder
public class DsQueryParam {

    private DataSourceType dataSourceType;

    private DeployEnvType  deployType;

    private LifeCycleState lifeCycleState;

    private String         dataSourceDescLike;

    private String         dsHostLike;

    private List<Long>     dataSourceIds;

    private String         instanceIdLike;

}
