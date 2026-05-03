package com.clougence.reactor.handlers.special;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.adapter.sqlserver.SqlServerAttributeNames;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.TableAfterMappingHandler;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

public class OceanBase2SqlServerHandler implements TableAfterMappingHandler {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        ETable srcTable = tableEditor.getSource();
        String srcTableCollation = MyUmiAttributeNames.COLLATION.getValue(srcTable.getAttribute());
        String srcTableCharacter = MyUmiAttributeNames.CHARACTER_SET.getValue(srcTable.getAttribute());

        for (String colName : tableEditor.getColumnNames()) {
            TableEditor.ColumnEditor columnEditor = tableEditor.getColumn(colName);
            EColumn srcColumn = columnEditor.getSource();
            SqlServerTypes tarType = SqlServerTypes.valueOfCode(srcColumn.getDbType());
            switch (tarType) {
                case VARCHAR:
                case NVARCHAR:
                case TEXT:
                case NTEXT:
                    String srcColCollation = MyUmiAttributeNames.DEFAULT_COLLATION_NAME.getValue(srcTable.getAttribute());
                    srcColCollation = StringUtils.isNotBlank(srcColCollation) ? srcColCollation : srcTableCollation;

                    String srcColCharacter = MyUmiAttributeNames.DEFAULT_CHARACTER_SET_NAME.getValue(srcTable.getAttribute());
                    srcColCharacter = StringUtils.isNotBlank(srcColCharacter) ? srcColCharacter : srcTableCharacter;

                    columnEditor.addAttr(SqlServerAttributeNames.COLLATION_NAME.getCodeKey(), mappingCharacter(srcColCollation, srcColCharacter));
                    break;
                default:
                    break;
            }
        }
    }

    private String mappingCharacter(String srcColCollation, String srcColCharacter) {
        if (StringUtils.containsIgnoreCase(srcColCollation, "utf8") || StringUtils.containsIgnoreCase(srcColCharacter, "utf8")) {
            return "Chinese_PRC_CI_AS";
        } else {
            return null;
        }
    }
}
