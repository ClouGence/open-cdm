package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsSessionDO;

/**
 * @author mode create time is 2020/4/13
 **/
public interface DmDsSessionMapper extends BaseMapper<DmDsSessionDO> {

    DmDsSessionDO queryBySessionId(String uid, String sessionId);

    List<DmDsSessionDO> queryByUser(String uid);

    DmDsSessionDO queryByWsnSessionId(String sessionId, String wsn);

    int updateSessionQueryTime(String sessionId, String wsn);

    int deleteBySessionId(String sessionId);

    Integer getUserSessionCount(String userId);

    int updateSessionConfig(DmDsSessionDO sessionDO);

    int updateAttachment(String sessionId, String data);
}
