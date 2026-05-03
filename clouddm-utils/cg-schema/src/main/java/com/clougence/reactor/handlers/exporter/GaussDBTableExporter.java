package com.clougence.reactor.handlers.exporter;

import com.clougence.adapter.gauss.GaussDBTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;

public class GaussDBTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

    }

    @Override
    public void beforeMapping(TableEditor tableEditor, TableEditor.ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // special type
        processSpecialType(columnEditor, dstType);
    }

    private void processSpecialType(TableEditor.ColumnEditor columnEditor, DsType dstType) {
        EColumn eColumn = columnEditor.getSource();
        if (eColumn.getDbType() == null) {
            return;
        }

        GaussDBTypes pgType = GaussDBTypes.valueOfCode(eColumn.getDbType());
        if (pgType == GaussDBTypes.UUID || pgType == GaussDBTypes.UUID_ARRAY) {
            eColumn.setLength(36L);
        }

        if (pgType == GaussDBTypes.NUMERIC && eColumn.getNumericPrecision() == null && eColumn.getNumericScale() == null) {
            columnEditor.setNumberLength(64, 16);
            columnEditor.addAttr(RdbAttributeNames.NUMBER_NO_SCALE_PREC.getCodeKey(), "true");
        }

        if (pgType == GaussDBTypes.MONEY) {
            columnEditor.setNumberLength(22, 2);
        }
    }
}
