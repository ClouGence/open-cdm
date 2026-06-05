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
package com.clougence.clouddm.console.web.model.fo.editor.data;

import java.util.List;

import com.clougence.clouddm.console.web.model.vo.editor.data.DataEditorColumnVO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecuteSqlFO {

    @NotNull(message = "levels must not null")
    private List<String>             levels;

    @NotBlank(message = "targetName must not blank")
    private String                   targetName;

    @NotBlank(message = "targetType must not blank")
    private String                   targetType;

    private List<DataEditorColumnVO> columnList;

    private ChangeRowFO              changeRow;
}
