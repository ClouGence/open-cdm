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
package com.clougence.reactor.handlers.exporter;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/1/8 19:56
 */
public class SqlServerTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    private static final String PREFIX = "CC_";

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

    }

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        if (srcType == dstType) {
            return;
        }

        // upgradeMax
        upgradeMax(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);

        // special type (sysname/uniqueidentifier/text/image/ntext)
        processSpecialType(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);

        // auto gen unique index
        alertPkUk(tableEditor, dstType);
    }

    private void processSpecialType(ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        SqlServerTypes sqlType = SqlServerTypes.valueOfCode(columnEditor.getSource().getDbType());
        switch (sqlType) {
            // The sysname data type is used for table columns, variables, and stored procedure parameters that store object names.
            // The exact definition of sysname is related to the rules for identifiers. Therefore, it can vary between instances of SQL Server.
            // sysname is functionally the same as nvarchar(128) except that, by default, sysname is NOT NULL.
            // In earlier versions of SQL Server, sysname is defined as varchar(30).
            case SYSNAME: {
                columnEditor.setCharLength(128L);
                columnEditor.setNullable(false);
                break;
            }
            case UNIQUEIDENTIFIER: {
                columnEditor.setCharLength(36L);
                break;
            }
            case TINYINT: {
                columnEditor.changeType(SqlServerTypes.TINYINT.getCodeKey()); // sqlserver 0~255
                columnEditor.setUnsigned(true);
                break;
            }
            case TEXT:
            case NTEXT:
            case IMAGE: {
                columnEditor.setCharLength(null);
                break;
            }
            // TIMESTAMP ROWVERSION is 8 bytes data.
            case TIMESTAMP:
            case ROWVERSION: {
                columnEditor.changeType(SqlServerTypes.BIGINT.getCodeKey());
                columnEditor.setNumberLength(21, 0);
                columnEditor.setUnsigned(true);
                break;
            }
            case SMALLMONEY: {
                columnEditor.setNumberLength(13, 4);
                break;
            }
            case MONEY: {
                columnEditor.setNumberLength(22, 4);
                break;
            }
        }
    }

    private void upgradeMax(ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        SqlServerTypes sqlType = SqlServerTypes.valueOfCode(columnEditor.getSource().getDbType());
        Long length = columnEditor.getSource().getLength();
        if (length != null && length > 0) {
            return;
        }

        switch (sqlType) {
            case VARCHAR:
                columnEditor.changeType(SqlServerTypes.TEXT.getCodeKey());
                columnEditor.setCharLength(null);
                break;
            case NVARCHAR:
                columnEditor.changeType(SqlServerTypes.NTEXT.getCodeKey());
                columnEditor.setCharLength(null);
                break;
            case VARBINARY:
                columnEditor.changeType(SqlServerTypes.IMAGE.getCodeKey());
                columnEditor.setCharLength(null);
                break;
            default:
                break;
        }
    }

    private void alertPkUk(TableEditor srcTableEditor, DsType dstType) {
        // only sqlserver -> mysql/mariadb (SELF_MAINTENANCE)
        if (dstType.equals(DsType.MySQL) || dstType.equals(DsType.MariaDB)) {
            ETable source = srcTableEditor.getSource();
            List<List<String>> uniqueList = new ArrayList<>();
            List<EIndex> indices = source.getIndices();
            for (EIndex eIndex : indices) {
                if (eIndex.getType().equals(EIndexType.Unique)) {
                    uniqueList.add(eIndex.getColumnList());
                }
            }

            // col witch describe auto inc without key
            for (EColumn ecloumn : source.getColumnList()) {
                TableEditor.PrimaryEditor primaryEditor = srcTableEditor.getPrimaryEditor();
                if (ecloumn.isAutoGenerate() && (primaryEditor == null || !primaryEditor.getSource().getColumnList().contains(ecloumn.getName()))) {
                    // judge already in any unique index
                    if (uniqueList.stream().noneMatch(unIndex -> unIndex.contains(ecloumn.getName()))) {
                        EIndex eIndex = new EIndex();
                        eIndex.setName(idxName(PREFIX, source.getSchema(), ecloumn.getName(), true));
                        eIndex.setType(EIndexType.Unique);
                        eIndex.getColumnList().add(ecloumn.getName());
                        source.getIndices().add(eIndex);
                    }
                }
            }
        }
    }

    public String idxName(String prefix, String tableName, String idxName, boolean rewrite) {
        return safeIdxName(prefix, tableName, idxName, rewrite);
    }

    private static String safeIdxName(String prefix, String tableName, String idxName, boolean rewrite) {
        if (!rewrite) {
            return idxName;
        }

        if (idxName.startsWith(prefix)) {
            idxName = idxName.substring(prefix.length());
        }
        String tmpName = prefix + "_" + idxName + "_for_" + tableName;
        tmpName = tmpName.replaceAll("[_]{2,}", "_");
        String hashName = String.valueOf(StringUtils.hashString(tmpName));

        if (tmpName.length() >= 18) {
            tmpName = tmpName.substring(0, 18);
        }

        return tmpName.endsWith("_") ? (tmpName + hashName) : (tmpName + "_" + hashName);
    }
}
