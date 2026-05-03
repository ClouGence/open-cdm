package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.FileStatus;
import com.clougence.clouddm.console.web.dal.model.DmFileDO;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmFileMapper extends BaseMapper<DmFileDO> {

    DmFileDO queryFileByUniqueId(String uniqueId);

    void updateStatusByUniqueId(String uniqueId, FileStatus toStatus, String message);

    void updateStatusByQueryId(String queryId, FileStatus toStatus, String message);

    List<DmFileDO> queryByAfterHeartbeatTime(Date point, int limit);

    void updateHeartbeatByUniqueId(String uniqueId);

    void updateAccessTimeByUniqueId(String uniqueId, String message);

    void incrementTryCountByUniqueId(String uniqueId, String message);

    int deleteFileByUniqueId(String uniqueId);

    int markFileDeletedByUniqueId(String uniqueId);
}
