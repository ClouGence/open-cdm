package com.clougence.reactor.handlers.exporter;

import java.sql.JDBCType;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.common.MappingNumberHelper;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/1/8 19:56
 */
public class MySqlTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

    }

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // decimal P/M completion
        decimalCompletion(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);

        MappingNumberHelper.myFamilyFloatDoubleCompletion(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);

        // bit upgrade
        bitUpgrade(columnEditor, dstType);

        // time Complete length
        timeComplete(columnEditor, srcType, dstType);
    }

    private void decimalCompletion(ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // https://dev.mysql.com/doc/refman/8.0/en/fixed-point-types.html
        if (srcType == dstType) {
            return;
        }

        String type = columnEditor.getSource().getDbType();
        if (type == null) {
            return;
        }

        if (!JDBCType.DECIMAL.getName().equals(type) && !JDBCType.NUMERIC.getName().equals(type)) {
            return;
        }

        Integer numericPrecision = columnEditor.getSource().getNumericPrecision();
        Integer numericScale = columnEditor.getSource().getNumericScale();

        if (numericPrecision == null) {
            numericPrecision = 10;// the default value of M is 10.
        }

        if (numericScale == null) {
            numericScale = 0;// In standard SQL, the syntax DECIMAL(M) is equivalent to DECIMAL(M,0)
        }

        if (numericScale != 0 && !columnEditor.getSource().getDbType().equals(JDBCType.DECIMAL.getName())) {
            columnEditor.changeType(MySQLTypes.DECIMAL.getCodeKey());
        }

        columnEditor.setNumberLength(numericPrecision, numericScale);
    }

    private void bitUpgrade(ColumnEditor columnEditor, DsType dstType) {
        EColumn eColumn = columnEditor.getSource();
        MySQLTypes mysqlTypes = MySQLTypes.valueOfCode(eColumn.getDbType());
        if (mysqlTypes != MySQLTypes.BIT) {
            return;
        }

        boolean dstSupportBit = dstType == DsType.MySQL //
                                || dstType == DsType.TiDB //
                                || dstType == DsType.PostgreSQL//
                                || dstType == DsType.Redshift//
                                || dstType == DsType.PolarDBMySQL //
                                || dstType == DsType.PolarDBPostgre //
                                || dstType == DsType.OceanBase //
                                || dstType == DsType.GreptimeDB //
                                || dstType == DsType.Paimon;
        if (dstSupportBit) {
            return;
        }

        String defaultValue = eColumn.getDefaultValue();
        if (StringUtils.isNotBlank(defaultValue)) {
            if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
                defaultValue = HexadecimalUtils.hex2bit(defaultValue.substring(2));
            } else if (defaultValue.startsWith("b'") || defaultValue.endsWith("'")) {
                defaultValue = defaultValue.substring(2, defaultValue.length() - 1);
            }
        }

        Integer numericPrecision = eColumn.getNumericPrecision();
        if (numericPrecision != null && numericPrecision == 1) {
            columnEditor.changeType(MySQLTypes.TINYINT.getCodeKey());
            columnEditor.setDefault(defaultValue, false);
        } else {
            columnEditor.changeType(MySQLTypes.TEXT.getCodeKey());
            columnEditor.setDefault(defaultValue, false);
        }
    }

    private void timeComplete(ColumnEditor columnEditor, DsType srcType, DsType dstType) {
        if (srcType == dstType && srcType == DsType.MySQL) {
            return;
        }

        EColumn eColumn = columnEditor.getSource();
        MySQLTypes type = MySQLTypes.valueOfCode(eColumn.getDbType());
        if (type != MySQLTypes.TIME) {
            return;
        }

        columnEditor.setCharLength(25L);// maybe is -888:22:22:123456789
    }
}
