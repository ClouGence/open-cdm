package com.clougence.reactor.handlers.exporter;

import com.clougence.adapter.postgre.PostgresTypes;
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
public class PostgresTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

    }

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // special type
        processSpecialType(columnEditor, dstType);
    }

    private void processSpecialType(ColumnEditor columnEditor, DsType dstType) {
        EColumn eColumn = columnEditor.getSource();
        if (eColumn.getDbType() == null) {
            return;
        }

        PostgresTypes pgType = PostgresTypes.valueOfCode(eColumn.getDbType());
        if (pgType == PostgresTypes.UUID || pgType == PostgresTypes.UUID_ARRAY) {
            eColumn.setLength(36L);
        }

        if (pgType == PostgresTypes.NUMERIC && eColumn.getNumericPrecision() == null && eColumn.getNumericScale() == null) {
            columnEditor.setNumberLength(64, 16);
            columnEditor.addAttr(RdbAttributeNames.NUMBER_NO_SCALE_PREC.getCodeKey(), "true");
        }

        if (pgType == PostgresTypes.MONEY) {
            columnEditor.setNumberLength(22, 2);
        }
    }
}
