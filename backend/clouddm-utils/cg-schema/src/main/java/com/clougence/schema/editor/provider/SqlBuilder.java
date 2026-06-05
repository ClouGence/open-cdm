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
package com.clougence.schema.editor.provider;

import com.clougence.schema.DsType;
import com.clougence.schema.editor.builder.mappings.DefaultMappingHandler;
import com.clougence.schema.editor.builder.mappings.MappingHandler;
import com.clougence.schema.editor.triggers.*;
import com.clougence.schema.metadata.MainVersion;

public interface SqlBuilder extends TableTriggers, ColumnTriggers, IndexTriggers, PrimaryKeyTriggers, ForeignKeyTriggers, ConstraintTriggers {

    DsType getDataSourceType();

    default MappingHandler createHandler(DsType tarDsType, MainVersion mainVersion) {
        return new DefaultMappingHandler(this.getDataSourceType(), tarDsType, mainVersion);
    }
}
