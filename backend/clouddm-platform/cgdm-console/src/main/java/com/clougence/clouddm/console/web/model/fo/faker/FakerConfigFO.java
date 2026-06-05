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
package com.clougence.clouddm.console.web.model.fo.faker;

import java.util.List;

import org.hibernate.validator.constraints.Range;

import com.clougence.clouddm.sdk.model.faker.FakerRunModel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
public class FakerConfigFO {

    @NotNull(message = "{faker.levels.notnull}")
    private List<String>       levels;

    @NotNull(message = "{faker.producer.thread.cnt.notnull}")
    @Range(max = 10, min = 1, message = "{faker.producer.thread.cnt.range}")
    private Integer            producer;

    @NotNull(message = "{faker.writer.thread.cnt.notnull}")
    @Range(max = 10, min = 1, message = "{faker.writer.thread.cnt.range}")
    private Integer            writer;

    private boolean            transaction;

    private boolean            ignoreErrors;

    @NotNull(message = "{faker.type.notnull}")
    private FakerRunModel      type;

    @NotNull(message = "{faker.ratio.insert.notnull}")
    private Integer            insertRatio;

    @NotNull(message = "{faker.ratio.update.notnull}")
    private Integer            updateRatio;

    @NotNull(message = "{faker.ratio.delete.notnull}")
    private Integer            deleteRatio;

    @NotNull(message = "{faker.running.time.notnull}")
    @Min(value = 1, message = "{faker.running.time.range}")
    private Integer            time;

    @NotNull(message = "{faker.config.table.notnull}")
    private List<FakerTableFO> tableConfigs;
}
