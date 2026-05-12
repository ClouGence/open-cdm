/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.rdp.constant.operation;

import com.clougence.clouddm.console.web.model.vo.AuditTypeVO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
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
        auditTypeVO.setAlias(DmI18nUtils.getMessage(this.name()));
        return auditTypeVO;
    }
}
