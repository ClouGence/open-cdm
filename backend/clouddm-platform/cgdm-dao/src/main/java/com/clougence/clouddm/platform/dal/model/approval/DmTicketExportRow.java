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

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 工单脚本导出行，对应 dm_approval 中一张工单。
 * 由 DmApprovalMapper.listTicketExportRows 返回，用于把一批工单脚本聚合成一个 .sql 文件。
 *
 * @author zhangfan
 */
@Getter
@Setter
public class DmTicketExportRow {

    /** 工单 ID */
    private Long   id;

    /** 工单标题 */
    private String ticketTitle;

    /** 绑定的数据源 ID */
    private Long   bindDsId;

    /** 创建时间 */
    private Date   gmtCreate;

    /** 状态 */
    private String status;

    /** SQL 脚本 */
    private String rawSql;

    /** 回滚脚本 */
    private String rollBackSql;

}
