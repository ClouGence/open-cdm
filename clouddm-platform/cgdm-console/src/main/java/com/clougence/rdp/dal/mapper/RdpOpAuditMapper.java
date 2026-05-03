package com.clougence.rdp.dal.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.dal.model.RdpOpAuditDO;

/**
 * @author bucketli 2020/2/29 12:00
 */
public interface RdpOpAuditMapper extends BaseMapper<RdpOpAuditDO> {

    List<RdpOpAuditDO> clearData();

    /**
     * query by basic condition.
     *
     * @param securityLevel optional
     * @param dateStart optional
     * @param dateEnd optional
     * @param startId not null
     * @param pageSize not null
     */
    List<RdpOpAuditDO> queryByConditionJoinUrlAuth(SecurityLevel securityLevel, Date dateStart, Date dateEnd, long startId, int pageSize);

    /**
     * query by uid
     *
     * @param uid           not null
     * @param securityLevel optional
     * @param auditType     optional
     * @param resourceType  optional
     * @param dateStart     optional
     * @param dateEnd       optional
     * @param startId       not null
     * @param pageSize      not null
     */
    List<RdpOpAuditDO> queryByUidJoinUrlAuth(String uid, SecurityLevel securityLevel, String auditType, String resourceType, Date dateStart, Date dateEnd, long startId,
                                             int pageSize);

    List<RdpOpAuditDO> queryByUidsJoinUrlAuth(String ownerUid, SecurityLevel securityLevel, String auditType, String resourceType, Date dateStart, Date dateEnd, long startId,
                                              int pageSize);

    List<RdpOpAuditDO> queryByCondition(String puid, String uid, SecurityLevel securityLevel, String auditType, String resourceType, String userNameLike, Date dateStart,
                                        Date dateEnd, long startId, int pageSize);

    List<RdpOpAuditDO> pageByCondition(String puid, String uid, SecurityLevel securityLevel, String auditType, String resourceType, String userNameLike, Date dateStart,
                                       Date dateEnd, int offset, int pageSize);

    List<RdpOpAuditDO> querySection(long startId, int pageSize);
}
