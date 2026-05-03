package com.clougence.reactor.handlers.special;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author mode 2021/1/8 19:56
 */
public class MySql2OracleHandler implements ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        MySQLTypes sqlType = MySQLTypes.valueOfCode(columnEditor.getSource().getDbType());
        if (sqlType == MySQLTypes.TIME) {
            columnEditor.changeType(MySQLTypes.VARCHAR.getCodeKey());
            columnEditor.setCharLength(32L);
        }
    }
}
