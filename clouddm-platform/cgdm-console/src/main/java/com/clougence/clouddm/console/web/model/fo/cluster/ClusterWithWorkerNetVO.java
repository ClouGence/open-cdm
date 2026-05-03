package com.clougence.clouddm.console.web.model.fo.cluster;

import java.util.Date;
import java.util.List;

import com.clougence.clouddm.console.web.constants.CloudOrIdcName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-20 12:28
 * @since 1.1.3
 */
@Getter
@Setter
public class ClusterWithWorkerNetVO {

    private Long              id;

    private Date              gmtCreate;

    private Date              gmtModified;

    private String            clusterName;

    private String            region;

    private CloudOrIdcName    cloudOrIdcName;

    private String            clusterDesc;

    private List<WorkerNetVO> workersNet;
}
