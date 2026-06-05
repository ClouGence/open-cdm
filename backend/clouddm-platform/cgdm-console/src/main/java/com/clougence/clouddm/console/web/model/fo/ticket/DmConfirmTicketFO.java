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

import com.clougence.clouddm.console.web.constants.DmConfirmActionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/5/9 14:35
*/
@Getter
@Setter
public class DmConfirmTicketFO {

    @Min(value = 1, message = "ticketId must large than 0.")
    private long                ticketId;

    @NotNull(message = "confirmActionType can not be null.")
    private DmConfirmActionType confirmActionType;

    @JsonIgnore
    private String              confirmUid;

    private String              comment;

    private DmAutoExecConfigFO  autoExecConfig;
}
