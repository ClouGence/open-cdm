package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.DmEventType;
import com.clougence.clouddm.console.web.dal.model.AlertConfigDetailDO;

/**
 * @author wanshao create time is 2020/4/13
 **/
public interface AlertConfigDetailMapper extends BaseMapper<AlertConfigDetailDO> {

    List<AlertConfigDetailDO> listByIds(@Param("ids") List<Long> ids);

    List<AlertConfigDetailDO> listByEventType(DmEventType eventType);

    List<AlertConfigDetailDO> listByEventTypes(List<DmEventType> eventTypes);

    void updateByIdAndUid(AlertConfigDetailDO detailDO);

    void updateByWorkerId(AlertConfigDetailDO detailDO);

    void updateSwitchByWorker(boolean phone, boolean email, boolean dingding, boolean sms, String uid, long workerId);

    AlertConfigDetailDO listByWorkerId(long workerId);

    void deleteByWorkerId(long workerId);

}
