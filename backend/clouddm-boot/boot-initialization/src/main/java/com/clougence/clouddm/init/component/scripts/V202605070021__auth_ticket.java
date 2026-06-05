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
package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070021__auth_ticket extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    create table if not exists rdp_ticket_auth
                        (
                            id                bigint auto_increment primary key,
                            gmt_create        datetime   default CURRENT_TIMESTAMP not null,
                            gmt_modified      datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
                            rdp_ticket_ins_id varchar(64)                          not null,
                            apply_auth_info   longtext                             not null,
                            kind_type         varchar(64)                          not null,
                            deleted           tinyint(1) default 0                 not null,
                            constraint idx_unique_biz_id unique (rdp_ticket_ins_id)
                        )
                """, """
                    alter table rdp_ticket_inst modify bind_ds_id bigint null
                """, """
                    drop index uk_path on rdp_res_auth
                """, """
                    create index idx_id_path_type_uid on rdp_res_auth (res_id, res_path, kind_type, owner_uid)
                """);
    }
}
