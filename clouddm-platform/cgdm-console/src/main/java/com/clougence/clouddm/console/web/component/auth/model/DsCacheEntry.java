package com.clougence.clouddm.console.web.component.auth.model;

import java.time.LocalDateTime;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/27 13:24:45
 */
@Getter
@Setter
public class DsCacheEntry {

    private Long           dsNumId;

    private String         dsInstId;

    private String         dsInstDesc;

    private DataSourceType dsType;

    private String         ownerUid;

    private Long           clusterId;

    private Long           envId;

    private LocalDateTime  addTime = LocalDateTime.now();
}
