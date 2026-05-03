package com.clougence.rdp.component.cache;

import com.clougence.rdp.component.cache.model.DsCacheEntry;
import com.clougence.rdp.component.cache.model.UserCacheEntry;

/**
 * @author bucketli 2024/2/27 13:19:28
 */
public interface RdpResOwnerCacheService {

    UserCacheEntry queryByUid(String uid);

    UserCacheEntry queryByAk(String ak);

    DsCacheEntry queryByDsId(Long dsId);

    void ownDataSource(String uid, Long dsId);
}
