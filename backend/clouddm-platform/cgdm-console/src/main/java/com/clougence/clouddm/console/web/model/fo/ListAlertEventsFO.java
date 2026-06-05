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
package com.clougence.clouddm.console.web.model.fo;

import com.clougence.clouddm.platform.dal.model.monitor.AlertEventStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * @author bucketli 2021/1/30 17:00
 */
@Data
public class ListAlertEventsFO {

    @Min(value = 0, message = "{min.startid}")
    long             startId;

    @Min(value = 1, message = "{min.pagesize}")
    @Max(value = PageData.MAX_PAGE_SIZE, message = "{max.pagesizen}" + PageData.MAX_PAGE_SIZE)
    int              pageSize;

    Long             leftTimeMillis;

    Long             rightTimeMillis;

    AlertEventStatus status;
}
