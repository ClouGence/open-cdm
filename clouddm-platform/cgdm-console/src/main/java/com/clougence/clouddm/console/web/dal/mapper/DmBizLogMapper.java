package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.DmLogDependBizType;
import com.clougence.clouddm.console.web.dal.model.exec.DmBizLogDO;

public interface DmBizLogMapper extends BaseMapper<DmBizLogDO> {

    List<DmBizLogDO> queryListByBizId(@Param("bizId") String bizId);

    List<DmBizLogDO> queryListByBizIdAndType(String bizId, DmLogDependBizType bizType);
}
