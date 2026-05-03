package com.clougence.reactor.handlers.importer;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.starrocks.StarRocksMainVersion;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.attributes.HandlersAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.TableEditor.PrimaryEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/1/8 19:56
 */
public class StarRocksTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // drop all index

        if (!StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), triggerContext.getAttribute(HandlersAttributeNames.SR_KEEP_INDEX))) {
            processDropAllIndex(tableEditor);
        }

        // non-primary columns set nullable = true
        processChangeColToNullable(tableEditor);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    protected void processChangeColToNullable(TableEditor edit) {
        PrimaryEditor primaryEditor = edit.getPrimaryEditor();
        List<String> pks = new ArrayList<>();
        if (primaryEditor != null) {
            pks.addAll(primaryEditor.getSource().getColumnList());
        }

        for (String colName : edit.getColumnNames()) {
            ColumnEditor colEditor = edit.getColumn(colName);
            if ((colEditor.getSource().getNullable() != null && colEditor.getSource().getNullable()) || pks.contains(colName)) {
                continue;
            }

            colEditor.setNullable(true);
        }
    }

    protected void processDropAllIndex(TableEditor edit) {
        for (String index : edit.getIndexes()) {
            TableEditor.IndexEditor indexEditor = edit.getIndexEditor(index);
            indexEditor.delete();
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn domain = columnEditor.getSource();
        StarRocksTypes sqlTypes = StarRocksTypes.valueOfCode(domain.getDbType());

        // CHAR / VARCHAR length extend size
        processExtendVarcharCharLength(sqlTypes, columnEditor, srcMainVersion);

        processUnsignedUpgrade(columnEditor, sqlTypes);

        processSpecialTypePkColumns(tableEditor, columnEditor);

        super.processDecimalType(columnEditor, sqlTypes);
    }

    private void processSpecialTypePkColumns(TableEditor tableEditor, ColumnEditor columnEditor) {
        PrimaryEditor primaryEditor = tableEditor.getPrimaryEditor();
        if (primaryEditor != null) {
            List<String> pkCols = primaryEditor.getSource().getColumnList();
            EColumn dstCol = columnEditor.getSource();
            if (pkCols != null && pkCols.contains(dstCol.getName())) {
                if (dstCol.getDbType().equals(StarRocksTypes.DECIMAL.getCodeKey())) {
                    columnEditor.changeType(StarRocksTypes.VARCHAR.getCodeKey());
                    columnEditor.setCharLength((long) (dstCol.getNumericPrecision() * 2));
                } else if (dstCol.getDbType().equals(StarRocksTypes.CHAR.getCodeKey())) {
                    columnEditor.changeType(StarRocksTypes.VARCHAR.getCodeKey());
                }
            }
        }
    }

    private void processUnsignedUpgrade(ColumnEditor columnEditor, StarRocksTypes sqlType) {
        EColumn eColumn = columnEditor.getSource();
        if (!eColumn.isNumericUnsigned()) {
            return;
        }
        switch (sqlType) {
            case TINYINT:
                columnEditor.changeType(StarRocksTypes.SMALLINT.getCodeKey());
                break;
            case SMALLINT:
                columnEditor.changeType(StarRocksTypes.INT.getCodeKey());
                break;
            case INT:
                columnEditor.changeType(StarRocksTypes.BIGINT.getCodeKey());
                break;
            case BIGINT:
                columnEditor.changeType(StarRocksTypes.LARGEINT.getCodeKey());
                break;
            case DECIMAL:
                columnEditor.changeType(StarRocksTypes.DECIMAL.getCodeKey());
                break;
            case FLOAT:
            case DOUBLE:
                columnEditor.changeType(StarRocksTypes.DOUBLE.getCodeKey());
                break;
            default:
                break;
        }
    }

    private void processExtendVarcharCharLength(StarRocksTypes sqlTypes, ColumnEditor columnEditor, MainVersion srcMainVersion) {
        switch (sqlTypes) {
            case CHAR: {
                Long sourceLength = columnEditor.getSource().getLength();
                if (sourceLength == null || (sourceLength * 3) > 255) {
                    columnEditor.setCharLength(255L);
                } else {
                    columnEditor.setCharLength(sourceLength * 3);
                }
            }
            case VARCHAR: {
                Long sourceLength = columnEditor.getSource().getLength();
                if (sourceLength == null || sourceLength <= 0) {
                    if (srcMainVersion != null && srcMainVersion.isGe(StarRocksMainVersion.StarRocks_2_1)) {
                        sourceLength = 1048576L;
                    } else {
                        sourceLength = 65535L;
                    }

                    columnEditor.setCharLength(sourceLength);
                } else {
                    columnEditor.setCharLength(sourceLength * 3);
                }
            }
        }
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return StarRocksTypes.valueOfCode(columnType);
    }
}
