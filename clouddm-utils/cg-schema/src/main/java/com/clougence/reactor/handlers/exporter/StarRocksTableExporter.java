package com.clougence.reactor.handlers.exporter;

import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author mode 2021/1/8 19:56
 */
public class StarRocksTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

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
        processLongVarchar(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);
    }

    private void processLongVarchar(ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        StarRocksTypes sqlType = StarRocksTypes.valueOfCode(columnEditor.getSource().getDbType());
        if (sqlType == StarRocksTypes.VARCHAR) {
            Long length = columnEditor.getSource().getLength();
            if (length >= 1280 * 3) {
                columnEditor.changeType(StarRocksTypes.STRING.getCodeKey());
                columnEditor.setCharLength(null);
            }
        }
    }
}
