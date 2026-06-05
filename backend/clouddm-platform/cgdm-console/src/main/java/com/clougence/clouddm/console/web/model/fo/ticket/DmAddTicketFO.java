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
package com.clougence.clouddm.console.web.model.fo.ticket;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/5/8 16:16
*/
@Getter
@Setter
public class DmAddTicketFO {

    @NotNull(message = "{ticket.dbLevels.notblank}")
    private List<String> dbLevels;

    @NotNull(message = "{ticket.sql.notnull}")
    private String       rawSql;

    private String       description;

    private String       rollBackSql;

    @NotBlank(message = "{ticket.title.notblank}")
    private String       ticketTitle;

    private Long         expectedAffectedRows;

    private boolean      force;

}
