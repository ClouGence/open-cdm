package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmSecRangeDO;

/**
 * @author Ekko
 * @date 2024/7/10 15:11
*/
public interface DmSecRangeMapper extends BaseMapper<DmSecRangeDO> {

    List<DmSecRangeDO> queryListBySpecId(String ownerUid, long specId);

    List<DmSecRangeDO> queryListByRefId(String ownerUid, long refId);

    int updateRange(String ownerUid, long rangeId, DmSecRangeDO info);

    int deleteRange(String ownerUid, long rangeId);

    void deleteBySpecId(String ownerUid, long specId);

    int deleteByRefId(String ownerUid, long refId);
}
