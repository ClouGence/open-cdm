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
package com.clougence.clouddm.sdk.ui.editor.table;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.schema.editor.domain.ETable;

public interface TableEditorUiDataSpi extends Spi {

    void fillETable(EditorViewMode viewMode, TableEditorUiData uiData, ETable eTable, String mainVersion);

    void fillUiData(EditorViewMode viewMode, ETable eTable, TableEditorUiData uiData, String mainVersion);
}
