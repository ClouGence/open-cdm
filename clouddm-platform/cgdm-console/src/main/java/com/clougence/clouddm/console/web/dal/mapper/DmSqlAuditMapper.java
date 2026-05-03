package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.console.web.dal.model.DmSqlAuditDO;
import com.clougence.clouddm.sdk.service.secrules.Requester;

public interface DmSqlAuditMapper extends BaseMapper<DmSqlAuditDO> {

    int updateBySessionId(@Param("sessionId") String sessionId, @Param("status") String status, @Param("affectLine") long affectLine, @Param("message") String message,
                          @Param("time") Date time);

    List<DmSqlAuditDO> queryByCondition(String puid, String uid, SecQueryKind sqlKind, String resourcePath, Long dsId, Requester requester, SqlStatus status, Date dateStart,
                                        Date dateEnd, long startId, int pageSize);

    void confirmSession(String sessionId);

    void rollbackSession(String sessionId);

    void updateErrorSql(@Param("wsn") String wsn);

    int deleteAuditBeforeDate(@Param("puid") String puid, @Param("date") Date date);
}
