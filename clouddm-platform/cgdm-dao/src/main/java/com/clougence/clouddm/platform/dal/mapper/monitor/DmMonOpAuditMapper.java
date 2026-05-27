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
package com.clougence.clouddm.platform.dal.mapper.monitor;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonOpAuditDO;

/**
 * @author bucketli 2020/2/29 12:00
 */
public interface DmMonOpAuditMapper extends BaseMapper<DmMonOpAuditDO> {

    List<DmMonOpAuditDO> clearData();

    /**
     * query by basic condition.
     *
     * @param securityLevel optional
     * @param dateStart optional
     * @param dateEnd optional
     * @param startId not null
     * @param pageSize not null
     */
    List<DmMonOpAuditDO> queryByConditionJoinUrlAuth(SecurityLevel securityLevel, Date dateStart, Date dateEnd, long startId, int pageSize);

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
    List<DmMonOpAuditDO> queryByUidJoinUrlAuth(String uid, SecurityLevel securityLevel, String auditType, String resourceType, Date dateStart, Date dateEnd, long startId,
                                             int pageSize);

    List<DmMonOpAuditDO> queryByUidsJoinUrlAuth(String ownerUid, SecurityLevel securityLevel, String auditType, String resourceType, Date dateStart, Date dateEnd, long startId,
                                              int pageSize);

    List<DmMonOpAuditDO> queryByCondition(String puid, String uid, SecurityLevel securityLevel, String auditType, String resourceType, String userNameLike, Date dateStart,
                                        Date dateEnd, long startId, int pageSize);

    List<DmMonOpAuditDO> pageByCondition(String puid, String uid, SecurityLevel securityLevel, String auditType, String resourceType, String userNameLike, Date dateStart,
                                       Date dateEnd, int offset, int pageSize);

    List<DmMonOpAuditDO> querySection(long startId, int pageSize);
}
