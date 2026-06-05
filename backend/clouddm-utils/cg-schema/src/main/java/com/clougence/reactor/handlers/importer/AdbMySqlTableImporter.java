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
package com.clougence.reactor.handlers.importer;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author mode 2021/1/8 19:56
 */
public class AdbMySqlTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // drop idx
        processIdx(tableEditor);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    private void processIdx(TableEditor tableEditor) {
        List<String> indexes = tableEditor.getIndexes();
        for (String idx : indexes) {
            tableEditor.getIndexEditor(idx).delete();
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        AdbMySQLTypes type = AdbMySQLTypes.valueOfCode(columnEditor.getSource().getDbType());
        // has unsigned upgrade to Uxxx
        processUnsignedUpgrade(columnEditor, type);
        processDecimalType(columnEditor, type);
    }

    private void processUnsignedUpgrade(ColumnEditor column, AdbMySQLTypes sqlType) {
        EColumn eColumn = column.getSource();
        boolean unsigned = eColumn.isNumericUnsigned();
        if (!unsigned) {
            return;
        }

        switch (sqlType) {
            case TINYINT:
                column.changeType(AdbMySQLTypes.SMALLINT.getCodeKey());
                break;
            case SMALLINT:
                column.changeType(AdbMySQLTypes.INT.getCodeKey());
                break;
            case INT:
                column.changeType(AdbMySQLTypes.BIGINT.getCodeKey());
                break;
            case BIGINT:
            case DECIMAL:
                column.changeType(AdbMySQLTypes.DECIMAL.getCodeKey());
                break;
            case FLOAT:
            case DOUBLE:
                column.changeType(AdbMySQLTypes.DOUBLE.getCodeKey());
                break;
            default:
                break;
        }
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return AdbMySQLTypes.valueOfCode(columnType);
    }
}
