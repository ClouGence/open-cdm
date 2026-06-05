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

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;

public class HanaTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, TableEditor.ColumnEditor columnEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion,
                              DsType dstType, MainVersion dstMainVersion) {
        HanaTypes types = HanaTypes.valueOfCode(columnEditor.getSource().getDbType());
        decimalCompletion(columnEditor, srcType, dstType, types);
        booleanPrecision(columnEditor, srcType, dstType, types);
    }

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
    }

    private void booleanPrecision(TableEditor.ColumnEditor columnEditor, DsType srcType, DsType dstType, HanaTypes types) {
        if (srcType != dstType && types == HanaTypes.BOOLEAN) {
            columnEditor.setNumberLength(1, 0);
        }
    }

    private void decimalCompletion(TableEditor.ColumnEditor columnEditor, DsType srcType, DsType dstType, HanaTypes types) {
        // https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/4ee2f261e9c44003807d08ccc2e249ac.html?q=BINARY
        if (srcType == dstType) {
            return;
        }

        Integer numericPrecision = columnEditor.getSource().getNumericPrecision();
        Integer numericScale = columnEditor.getSource().getNumericScale();
        // Hana supports floating-point decimal types，decimal() Representable range decimal(34,0)  decimal(0,34)
        // other datasource can not support, only support Fixed point decimal, so numericPrecision > 38 decimal convert varchar
        if (types == HanaTypes.DECIMAL) {
            if (numericPrecision == null && numericScale == null) {
                numericPrecision = 68;
                numericScale = 34;
            } else if (numericScale == null) {
                numericScale = numericPrecision;
                numericPrecision = numericPrecision + numericScale;
            }
        } else if (types == HanaTypes.SMALLDECIMAL) {
            numericPrecision = 32;
            numericScale = 16;
        }

        columnEditor.setNumberLength(numericPrecision, numericScale);
    }
}
