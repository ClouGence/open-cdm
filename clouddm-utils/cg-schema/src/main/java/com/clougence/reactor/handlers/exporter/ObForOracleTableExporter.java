package com.clougence.reactor.handlers.exporter;

import com.clougence.adapter.ob.obfororacle.ObForOracleTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;

/**
 * @author chunlin create time is 2024/9/11
 */
public class ObForOracleTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

    }

    @Override
    public void beforeMapping(TableEditor tableEditor, TableEditor.ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // decimal P/M completion
        typeCompletion(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);
    }

    private void typeCompletion(TableEditor.ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn eColumn = columnEditor.getSource();
        if (eColumn.getDbType() == null) {
            return;
        }

        ObForOracleTypes sqlType = ObForOracleTypes.valueOfCode(eColumn.getDbType());
        if (sqlType == ObForOracleTypes.NUMBER_DECIMAL && eColumn.getNumericPrecision() == null && eColumn.getNumericScale() == null) {
            columnEditor.setNumberLength(64, 16);
            columnEditor.addAttr(RdbAttributeNames.NUMBER_NO_SCALE_PREC.getCodeKey(), "true");
        } else if (sqlType == ObForOracleTypes.BINARY_FLOAT) {
            columnEditor.setNumberLength(8, 4);
        } else if (sqlType == ObForOracleTypes.BINARY_DOUBLE) {
            columnEditor.setNumberLength(16, 8);
        }
    }
}
