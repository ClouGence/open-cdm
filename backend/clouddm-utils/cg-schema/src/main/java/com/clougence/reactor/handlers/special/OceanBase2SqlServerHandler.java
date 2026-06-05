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

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.adapter.sqlserver.SqlServerAttributeNames;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.TableAfterMappingHandler;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

public class OceanBase2SqlServerHandler implements TableAfterMappingHandler {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        ETable srcTable = tableEditor.getSource();
        String srcTableCollation = MyUmiAttributeNames.COLLATION.getValue(srcTable.getAttribute());
        String srcTableCharacter = MyUmiAttributeNames.CHARACTER_SET.getValue(srcTable.getAttribute());

        for (String colName : tableEditor.getColumnNames()) {
            TableEditor.ColumnEditor columnEditor = tableEditor.getColumn(colName);
            EColumn srcColumn = columnEditor.getSource();
            SqlServerTypes tarType = SqlServerTypes.valueOfCode(srcColumn.getDbType());
            switch (tarType) {
                case VARCHAR:
                case NVARCHAR:
                case TEXT:
                case NTEXT:
                    String srcColCollation = MyUmiAttributeNames.DEFAULT_COLLATION_NAME.getValue(srcTable.getAttribute());
                    srcColCollation = StringUtils.isNotBlank(srcColCollation) ? srcColCollation : srcTableCollation;

                    String srcColCharacter = MyUmiAttributeNames.DEFAULT_CHARACTER_SET_NAME.getValue(srcTable.getAttribute());
                    srcColCharacter = StringUtils.isNotBlank(srcColCharacter) ? srcColCharacter : srcTableCharacter;

                    columnEditor.addAttr(SqlServerAttributeNames.COLLATION_NAME.getCodeKey(), mappingCharacter(srcColCollation, srcColCharacter));
                    break;
                default:
                    break;
            }
        }
    }

    private String mappingCharacter(String srcColCollation, String srcColCharacter) {
        if (StringUtils.containsIgnoreCase(srcColCollation, "utf8") || StringUtils.containsIgnoreCase(srcColCharacter, "utf8")) {
            return "Chinese_PRC_CI_AS";
        } else {
            return null;
        }
    }
}
