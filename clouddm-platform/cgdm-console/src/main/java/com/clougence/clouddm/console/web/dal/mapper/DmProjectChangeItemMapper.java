package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeItemType;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeItemDO;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmProjectChangeItemMapper extends BaseMapper<DmProjectChangeItemDO> {

    List<DmProjectChangeItemDO> queryChangeItemByChangeId(String ownerUid, long refChangeId, DmChangeItemType itemType);

    int deleteByChangeItemType(String ownerUid, long refChangeId, DmChangeItemType itemType);

    int deleteByChangeItemAll(String ownerUid, long refChangeId);
}
