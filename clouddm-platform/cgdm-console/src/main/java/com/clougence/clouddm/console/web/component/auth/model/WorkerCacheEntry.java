package com.clougence.clouddm.console.web.component.auth.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/27 13:20:36
 */
@Getter
@Setter
public class WorkerCacheEntry {

    private Long          workerNumId;

    private String        wsn;

    private String        ownerUid;

    private LocalDateTime addTime = LocalDateTime.now();
}
