package com.clougence.rdp.constant.operation;

import com.clougence.rdp.controller.model.vo.AuditTypeVO;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.JsonUtils;

public enum AuditType {

    //****** DATA SOURCE ******
    ADD_DATA_SOURCE,
    DELETE_DATA_SOURCE,
    QUERY_DATA_SOURCE_CONFIG,
    UPDATE_DATA_SOURCE_CONFIG,
    UPDATE_DATA_SOURCE_DESC,
    UPDATE_DS_ACCOUNT_PASSWD,
    DELETE_DS_ACCOUNT_PASSWD,
    //******* DATA JOB *******
    QUERY_JOB_INFO,
    UPDATE_PARAMS,
    UPDATE_SUBSCRIBE,
    UPDATE_SUBSCRIBE_FULL,
    CREATE_JOB,
    START_JOB,
    STOP_JOB,
    RESTART_JOB,
    DELETE_JOB,
    MANUAL_MERGE,
    REPLAY_JOB,
    UPDATE_POSITION,
    RESET_POSITION,
    QUERY_CURRENT_POSITION,
    ATTACH_INCRE_TASK,
    DETACH_INCRE_TASK,
    ADD_REVISE,
    ADD_CHECK,
    PAUSE_SCHEDULE,
    RESUME_SCHEDULE,
    START_SCHEDULE,
    ACTIVE_FSM,
    //******* SYSTEM CONFIG *******
    ADD_SUB_ACCOUNT,
    UPDATE_SUB_ACCOUNT,
    MODIFY_SUB_ACCOUNT_AUTH,
    ENABLE_SUB_ACCOUNT,
    DISABLE_SUB_ACCOUNT,
    UPDATE_SUB_ACCOUNT_PWD,
    UPDATE_SUB_ACCOUNT_ROLE,
    DELETE_SUB_ACCOUNT,
    CREATE_ROLE,
    UPDATE_ROLE,
    DELETE_ROLE,
    ADD_DS_ENV,
    UPDATE_DS_ENV,
    DELETE_DS_ENV,
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT,
    QUERY_ACCOUNT_AK_SK,
    RESET_ACCOUNT_AK_SK,
    UPDATE_ACCOUNT_EMAIL,
    UPDATE_ACCOUNT_PHONE,
    UPDATE_ACCOUNT_PWD,
    UPDATE_ACCOUNT_OP_PWD,

    UPDATE_SYSTEM_CONFIG,
    AUTHORIZE_ACCESS_TO_ALIYUN,
    REVOKE_ACCESS_TO_ALIYUN;

    public static String genUK(AuditType type, String UUIDKey, Object resId) {
        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setType(type);
        operationDTO.setKey(UUIDKey);
        operationDTO.setResId(String.valueOf(resId));
        return JsonUtils.toJson(operationDTO);
    }

    public AuditTypeVO convertAuditType() {
        AuditTypeVO auditTypeVO = new AuditTypeVO();
        auditTypeVO.setAuditType(this.name());
        auditTypeVO.setAlias(RdpI18nUtils.getMessage(this.name()));
        return auditTypeVO;
    }
}
