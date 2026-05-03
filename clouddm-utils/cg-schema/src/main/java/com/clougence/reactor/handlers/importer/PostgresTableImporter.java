package com.clougence.reactor.handlers.importer;

import static com.clougence.reactor.handlers.attributes.HandlersAttributeNames.PG_BIT1_2_BOOLEAN;
import static com.clougence.reactor.handlers.attributes.HandlersAttributeNames.PG_ZERO_TIME_STRATEGY;

import java.util.ArrayList;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.attributes.TimeDefaultStrategyEnum;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/1/8 19:56
 */
public class PostgresTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // drop idx with expression
        processExpressionIndex(tableEditor);

        // pk/uk/index duplicate column name.
        super.processIdxDuplicateColumnName(tableEditor);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

        // process bit 1
        processBit1(tableEditor, columnEditor, triggerContext);

        // zero time
        processZeroTime(tableEditor, columnEditor, triggerContext);

        // has unsigned upgrade to Uxxx
        processUnsignedUpgrade(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);
    }

    private void processBit1(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext) {
        EColumn domain = columnEditor.getSource();
        PostgresTypes sqlTypes = PostgresTypes.valueOfCode(domain.getDbType());
        if (sqlTypes != PostgresTypes.BIT) {
            return;
        }

        String defaultConf = triggerContext.getAttribute(PG_BIT1_2_BOOLEAN);
        boolean enable = StringUtils.isBlank(defaultConf) || StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), defaultConf);
        Integer precision = columnEditor.getSource().getNumericPrecision();

        if (precision == 1 && enable) {
            columnEditor.changeType(PostgresTypes.BOOLEAN.getCodeKey());
        }
    }

    private void processZeroTime(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext) {
        TimeDefaultStrategyEnum strategy = TimeDefaultStrategyEnum.valueOfCode(triggerContext.getAttribute(PG_ZERO_TIME_STRATEGY));
        EColumn domain = columnEditor.getSource();
        PostgresTypes sqlTypes = PostgresTypes.valueOfCode(domain.getDbType());

        String currentFunc = "";
        switch (sqlTypes) {
            case TIMESTAMP_WITHOUT_TIME_ZONE:
                currentFunc = "localtimestamp";
                break;
            case TIMESTAMP_WITH_TIME_ZONE:
                currentFunc = "current_timestamp";
                break;
            case TIME_WITHOUT_TIME_ZONE:
                currentFunc = "localtime";
                break;
            case TIME_WITH_TIME_ZONE:
                currentFunc = "current_time";
                break;
            case DATE:
                currentFunc = "current_date";
                break;
            case INTERVAL: // TODO complex time types
            default:
                return;
        }

        String defaultValue = domain.getDefaultValue();
        boolean zeroTime = StringUtils.startsWith(defaultValue, "0000-00-00 00:00:00")//
                           || StringUtils.startsWith(defaultValue, "0000-00-00") //
                           || StringUtils.startsWith(defaultValue, "00:00:00");
        if (zeroTime) {
            if (strategy == TimeDefaultStrategyEnum.CURRENT) {
                columnEditor.setNullable(false);
                columnEditor.setDateTimePrecision(0);
                columnEditor.setDefault(currentFunc, true);
            } else if (strategy == TimeDefaultStrategyEnum.UTC_ZERO) {
                columnEditor.setNullable(false);
                columnEditor.setDateTimePrecision(0);
                columnEditor.setDefault("1970-01-01 00:00:00", false);
            } else if (strategy == TimeDefaultStrategyEnum.IS_NULL) {
                columnEditor.setNullable(true);
                columnEditor.setDateTimePrecision(0);
                columnEditor.setDefault("", false);
            } else if (strategy == TimeDefaultStrategyEnum.NOTHING) {

            }
        }
    }

    private void processUnsignedUpgrade(ColumnEditor column, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn eColumn = column.getSource();
        boolean unsigned = eColumn.isNumericUnsigned();
        if (!unsigned) {
            return;
        }

        PostgresTypes sqlType = PostgresTypes.valueOfCode(eColumn.getDbType());
        switch (sqlType) {
            case SMALLINT:
                column.changeType(PostgresTypes.INTEGER.getCodeKey());
                break;
            case INTEGER:
                column.changeType(PostgresTypes.BIGINT.getCodeKey());
                break;
            case BIGINT:
            case NUMERIC:
                column.changeType(PostgresTypes.NUMERIC.getCodeKey());
                break;
            case REAL:
            case DOUBLE_PRECISION:
                column.changeType(PostgresTypes.DOUBLE_PRECISION.getCodeKey());
                break;
            default:
                break;
        }
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return PostgresTypes.valueOfCode(columnType);
    }
}
