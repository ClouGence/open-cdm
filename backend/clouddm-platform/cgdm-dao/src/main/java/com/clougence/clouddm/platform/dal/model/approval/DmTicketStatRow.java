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
package com.clougence.clouddm.platform.dal.model.approval;

import lombok.Getter;
import lombok.Setter;

/**
 * 工单按数据源(数据库)汇总的统计行。
 * 由 DmApprovalMapper.statTicketByDs 返回，一条 = 某个数据源下某状态的工单数。
 *
 * @author zhangfan
 */
@Getter
@Setter
public class DmTicketStatRow {

    /** 绑定的数据源 ID（dm_approval.bind_ds_id） */
    private Long   bindDsId;

    /** 工单状态（dm_approval.ticket_status） */
    private String status;

    /** 数量 */
    private Long   cnt;

}
