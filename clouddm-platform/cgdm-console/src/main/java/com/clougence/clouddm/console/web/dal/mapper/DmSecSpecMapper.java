package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmSecSpecDO;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
public interface DmSecSpecMapper extends BaseMapper<DmSecSpecDO> {

    List<DmSecSpecDO> searchSpec(String ownerUid, String searchKeywords);

    List<DmSecSpecDO> queryByUid(String ownerUid);

    DmSecSpecDO queryByIdAndUid(String ownerUid, long specId);

    int deleteByUidAndId(String ownerUid, long specId);

    int enableSpec(String ownerUid, long specId);

    int disableSpec(String ownerUid, long specId);

    int updateInfo(String ownerUid, long specId, String newName, String newDesc);

    List<DmSecSpecDO> queryByIds(String ownerUid, @Param("ids") Collection<Long> secIds);
}
