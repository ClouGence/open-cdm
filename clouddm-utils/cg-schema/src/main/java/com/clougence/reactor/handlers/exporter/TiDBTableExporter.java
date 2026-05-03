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
