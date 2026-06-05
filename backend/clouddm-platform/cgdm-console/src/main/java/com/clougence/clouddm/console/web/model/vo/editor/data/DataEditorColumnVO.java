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
package com.clougence.clouddm.console.web.model.vo.editor.data;

import java.util.List;

import com.clougence.clouddm.sdk.ui.editor.data.DataEditorHeader;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorUiStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEditorColumnVO {

    private String                 column;

    private String                 columnType;

    private String[]               option;

    private Boolean                isUk;

    private Boolean                isPk;

    private DataEditorUiStyle      uiType;

    private Boolean                hasDefault;

    private Boolean                isNullable;

    private Long                   length;

    private Integer                numericPrecision;

    private Integer                numericScale;

    private Boolean                autoincrement  = false;

    private Boolean                whereKey       = false;

    private Boolean                spareWhere     = true;

    private Boolean                insertReadOnly = false;

    private Boolean                updateReadOnly = false;

    private Boolean                hide           = false;

    private List<DataEditorHeader> check;
}
