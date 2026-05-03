package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmSecSensitiveDO;

/**
 * @author Ekko
 * @date 2024/7/10 15:11
*/
public interface DmSecSensitiveMapper extends BaseMapper<DmSecSensitiveDO> {

    DmSecSensitiveDO queryInnerBySenStrId(String senStrId);

    void markInnerDeprecatedById(String senStrId);

    int updateInnerSenById(long senId, DmSecSensitiveDO senDO);

    List<DmSecSensitiveDO> searchSens(String ownerUid, String searchKeywords);

    List<DmSecSensitiveDO> listByUid(String ownerUid);

    DmSecSensitiveDO queryById(String ownerUid, long senId);

    int deleteByUidAndId(String ownerUid, long senId);

    int updateSen(String ownerUid, long senId, DmSecSensitiveDO senDO);

    List<DmSecSensitiveDO> queryByIds(String ownerUid, Collection<Long> ids);
}
