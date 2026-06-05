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

import java.util.List;

import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/1/8 19:56
 */
public class ObTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

    }

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // may have more problem, uk column can be empty, but primary key can not.
        // upgradeUkToPk(tableEditor, srcType, dstType);

        bitUpgrade(columnEditor, dstType);

        // time Complete length
        timeComplete(columnEditor, srcType, dstType);
    }

    private void bitUpgrade(ColumnEditor columnEditor, DsType dstType) {
        EColumn eColumn = columnEditor.getSource();
        ObForMySQLTypes obType = ObForMySQLTypes.valueOfCode(eColumn.getDbType());
        if (obType != ObForMySQLTypes.BIT) {
            return;
        }

        boolean dstSupportBit = dstType == DsType.MySQL //
                                || dstType == DsType.TiDB //
                                || dstType == DsType.PostgreSQL//
                                || dstType == DsType.PolarDBMySQL //
                                || dstType == DsType.PolarDBPostgre //
                                || dstType == DsType.OceanBase //
                                || dstType == DsType.SqlServer // bit -> varbinary
                                || dstType == DsType.StarRocks // bit -> int
                                || dstType == DsType.Doris // bit -> int
                                || dstType == DsType.SelectDB // bit -> varbinary
                                || dstType == DsType.ClickHouse // bit -> int64
        ;
        if (dstSupportBit) {
            return;
        }

        String defaultValue = eColumn.getDefaultValue();
        if (StringUtils.isNotBlank(defaultValue)) {
            if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                defaultValue = HexadecimalUtils.hex2bit(defaultValue.substring(2));
            } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                defaultValue = defaultValue.substring(2, defaultValue.length() - 1);
            }
        }

        Integer numericPrecision = eColumn.getNumericPrecision();
        if (numericPrecision != null && numericPrecision == 1) {
            columnEditor.changeType(ObForMySQLTypes.TINYINT.getCodeKey());
            columnEditor.setDefault(defaultValue, false);
        } else {
            columnEditor.changeType(ObForMySQLTypes.TEXT.getCodeKey());
            columnEditor.setDefault(defaultValue, false);
        }
    }

    private void timeComplete(ColumnEditor columnEditor, DsType srcType, DsType dstType) {
        if (srcType == dstType && srcType == DsType.OceanBase) {
            return;
        }

        EColumn eColumn = columnEditor.getSource();
        ObForMySQLTypes type = ObForMySQLTypes.valueOfCode(eColumn.getDbType());
        if (type != ObForMySQLTypes.TIME) {
            return;
        }

        columnEditor.setCharLength(25L);// maybe is -888:22:22:123456789
    }

    protected void upgradeUkToPk(TableEditor tableEditor, DsType srcType, DsType dstType) {
        if (srcType == dstType) {
            return;
        }

        if (tableEditor.getPrimaryEditor() != null) {
            return;
        }

        TableEditor.IndexEditor target = null;
        for (String index : tableEditor.getIndexes()) {
            TableEditor.IndexEditor indexEditor = tableEditor.getIndexEditor(index);
            if (indexEditor.getSource().getType() == EIndexType.Unique) {
                target = indexEditor;
                break;
            }
        }

        if (target == null) {
            return;
        }

        List<String> ukCols = target.getSource().getColumnList();
        tableEditor.createPrimaryEditor(ukCols);
        target.delete();
    }
}
