package com.clougence.rdp.component.cache.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/27 13:24:45
 */
@Getter
@Setter
public class DsCacheEntry {

    private Long          dsNumId;

    private String        dsInstId;

    private String        ownerUid;

    private LocalDateTime addTime = LocalDateTime.now();
}
