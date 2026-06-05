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

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;

/**
 * @author mode 2021/1/8 19:56
 */
public class OracleTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

    }

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // decimal P/M completion
        typeCompletion(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);
    }

    private void typeCompletion(ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn eColumn = columnEditor.getSource();
        if (eColumn.getDbType() == null) {
            return;
        }

        OracleSqlTypes sqlType = OracleSqlTypes.valueOfCode(eColumn.getDbType());
        if (sqlType == OracleSqlTypes.NUMBER_DECIMAL && eColumn.getNumericPrecision() == null && eColumn.getNumericScale() == null) {
            columnEditor.setNumberLength(64, 16);
            columnEditor.addAttr(RdbAttributeNames.NUMBER_NO_SCALE_PREC.getCodeKey(), "true");
        } else if (sqlType == OracleSqlTypes.BINARY_FLOAT) {
            columnEditor.setNumberLength(8, 4);
        } else if (sqlType == OracleSqlTypes.BINARY_DOUBLE) {
            columnEditor.setNumberLength(16, 8);
        } else if (dstType != DsType.Oracle && sqlType == OracleSqlTypes.NUMBER_DECIMAL && eColumn.getNumericPrecision() == null && eColumn.getNumericScale() >= 0) {
            // if target is not oracle, need fill default precision
            // number(*) => numericPrecision = null, numericScale = 0
            // number(*,1) => numericPrecision = null, numericScale = 1
            columnEditor.setNumberLength(38, eColumn.getNumericScale());
        }
    }
}
