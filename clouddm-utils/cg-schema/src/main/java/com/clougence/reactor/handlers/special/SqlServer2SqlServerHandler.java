package com.clougence.reactor.handlers.special;

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author mode 2021/1/8 19:56
 */
public class SqlServer2SqlServerHandler implements ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        SqlServerTypes sqlType = SqlServerTypes.valueOfCode(columnEditor.getSource().getDbType());

        if (sqlType == SqlServerTypes.TIMESTAMP || sqlType == SqlServerTypes.ROWVERSION) {
            columnEditor.changeType(SqlServerTypes.NUMERIC.getCodeKey());
            columnEditor.setNumberLength(21, 0);
        }

        if (sqlType == SqlServerTypes.NVARCHAR && columnEditor.getSource().getLength() == 0) {
            columnEditor.setCharLength(-1L);
        }
    }
}
