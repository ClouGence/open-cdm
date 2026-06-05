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

import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author mode 2021/1/8 19:56
 */
public class TiDBTableExporter implements ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // bit upgrade
        bitUpgrade(columnEditor, dstType);

        // float upgrade
        floatUpgrade(columnEditor, dstType);
    }

    private void floatUpgrade(ColumnEditor columnEditor, DsType dstType) {
        if (dstType == DsType.MySQL) {
            EColumn col = columnEditor.getSource();
            TiDBTypes tidbTypes = TiDBTypes.valueOfCode(col.getDbType());
            if (tidbTypes == TiDBTypes.FLOAT && col.getNumericScale() == null) {
                col.setNumericScale(2);
            }
        }
    }

    private void bitUpgrade(ColumnEditor columnEditor, DsType dstType) {
        EColumn eColumn = columnEditor.getSource();
        TiDBTypes tidbTypes = TiDBTypes.valueOfCode(eColumn.getDbType());
        if (tidbTypes != TiDBTypes.BIT) {
            return;
        }

        boolean dstSupportBit = dstType == DsType.MySQL //
                                || dstType == DsType.TiDB //
                                || dstType == DsType.PostgreSQL//
                                || dstType == DsType.PolarDBMySQL //
                                || dstType == DsType.PolarDBPostgre //
                                || dstType == DsType.OceanBase;

        if (dstSupportBit) {
            return;
        }

        Integer precision = eColumn.getNumericPrecision();
        if (precision == null || precision != 1) {
            if (dstType == DsType.StarRocks || dstType == DsType.Doris) {
                columnEditor.changeType(TiDBTypes.VARCHAR.getCodeKey());
            } else {
                columnEditor.changeType(TiDBTypes.TEXT.getCodeKey());
            }
        }
    }
}
