package com.clougence.rdp.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.controller.model.fo.ExportOpAuditFO;
import com.clougence.rdp.controller.model.vo.OpAuditConditionVO;
import com.clougence.rdp.controller.model.vo.RdpOpAuditVO;
import com.clougence.rdp.dal.model.RdpOpAuditDO;

/**
 * @author bucketli 2020/4/13 12:53
 */
public interface RdpOpAuditService {

    int    DEFAULT_PAGE_SIZE   = 20;

    int    MAX_PAGE_SIZE       = 60;

    String QUERY_CONDITION_CC  = "cloudcanal";

    String QUERY_CONDITION_RDP = "rdp";

    String QUERY_CONDITION_DM  = "clouddm";

    /**
     * add OperationAuditDO data to database.ORIGIN DATA, not referent any other metadata.
     */
    void addOperationAudit(RdpOpAuditDO auditDO);

    List<RdpOpAuditVO> queryUserAllAudit(String puid, String uid, SecurityLevel securityLevel, String userNameLike, String auditType, String resourceType, Date start, Date end,
                                         long startId, int pageSize);

    /**
     * query audit by uid and basic condition. if startId not specified, fill it with 0. if pageSize not specified, fill it with DEFAULT_PAGE_SIZE.
     *
     * @param uid           not be null
     * @param securityLevel optional
     * @param auditType     optional
     * @param resourceType  optional
     * @param start         optional
     * @param end           optional
     * @param startId       optional
     * @param pageSize      optional
     */
    List<RdpOpAuditVO> findAuditByUid(String uid, SecurityLevel securityLevel, String auditType, String resourceType, Date start, Date end, long startId, int pageSize);

    /**
     * query audit by userName and basic condition. if startId not specified, fill it with 0. if pageSize not specified, fill it with DEFAULT_PAGE_SIZE.
     *
     * @param userName      not be null
     * @param securityLevel optional
     * @param auditType     optional
     * @param resourceType  optional
     * @param start         optional
     * @param end           optional
     * @param startId       optional
     * @param pageSize      optional
     */
    List<RdpOpAuditVO> findAuditByUserName(String puid, String userName, SecurityLevel securityLevel, String auditType, String resourceType, Date start, Date end, long startId,
                                           int pageSize);

    void logAndAddOperationAudit(String puid, String uid, String requestUri, String remoteAddr, Object resId, Object obj, SecurityLevel securityLevel, AuditType auditType,
                                 ResourceType resType);

    void logAndAddOperationAudit(String puid, String uid, String requestUri, String remoteAddr, Object resId, Object obj, SecurityLevel securityLevel, AuditType auditType,
                                 ResourceType resType, String oldName);

    OpAuditConditionVO queryListCondition(String conditionType);

    Boolean isExistsOpAuditLog(String auditType);

    void exportAuditLog(ExportOpAuditFO exportOpAuditFO, HttpServletResponse response);
}
