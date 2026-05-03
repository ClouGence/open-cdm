package com.clougence.rdp.component.cache.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/27 13:20:36
 */
@Getter
@Setter
public class UserCacheEntry {

    private Long          userNumId;

    private String        uid;

    private String        ak;

    private LocalDateTime addTime = LocalDateTime.now();
}
