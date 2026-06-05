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
package com.clougence.reactor.handlers.special;

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author mode 2021/1/8 19:56
 */
public class SqlServer2SqlServerHandler implements ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        SqlServerTypes sqlType = SqlServerTypes.valueOfCode(columnEditor.getSource().getDbType());

        if (sqlType == SqlServerTypes.TIMESTAMP || sqlType == SqlServerTypes.ROWVERSION) {
            columnEditor.changeType(SqlServerTypes.NUMERIC.getCodeKey());
            columnEditor.setNumberLength(21, 0);
        }

        if (sqlType == SqlServerTypes.NVARCHAR && columnEditor.getSource().getLength() == 0) {
            columnEditor.setCharLength(-1L);
        }
    }
}
