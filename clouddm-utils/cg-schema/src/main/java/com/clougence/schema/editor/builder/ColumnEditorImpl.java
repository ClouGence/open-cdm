package com.clougence.schema.editor.builder;

import java.util.*;

import com.clougence.schema.editor.ConflictException;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.TableEditor.ForeignKeyEditor;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EForeignKey;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/5/21 19:56
 */
class ColumnEditorImpl extends AbstractEditor implements ColumnEditor {

    private final ETable  eTable;
    private final EColumn eColumn;

    public ColumnEditorImpl(EditorContext context, ETable eTable, EColumn eColumn){
        super(context);
        this.eTable = eTable;
        this.eColumn = eColumn;
    }

    @Override
    public EColumn getSource() { return this.eColumn; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }

    @Override
    public void rename(String newName) {
        if (StringUtils.isBlank(newName)) {
            throw new NullPointerException("new name is null.");
        }

        String oldName = this.eColumn.getName();
        if (StringUtils.equals(oldName, newName)) {
            return;
        }

        ColumnEditor columnEditor = new TableEditorImpl(this.context, this.eTable).getColumn(newName);
        if (columnEditor != null) {
            throw new ConflictException("column '" + newName + " already exists.");
        }
        SqlBuilder provider = context.getTarDsInfo().getProvider();
        if (!context.getTarDsInfo().getProvider().supportColumnRename()) {
            throw new UnsupportedOperationException("provider " + provider.getClass() + " column rename Unsupported.");
        }

        boolean reAddPk = false;
        List<String> reAddIndex = new ArrayList<>();
        Map<String, String> reAddFk = new LinkedHashMap<>();
        Map<String, EIndex> oldIdxInfo = new LinkedHashMap<>();
        Map<String, EForeignKey> oldFkInfo = new LinkedHashMap<>();

        new ArrayList<>(this.eTable.getForeignKeys()).forEach(eForeignKey -> {
            if (eForeignKey.getColumnList().contains(oldName)) {
                String refMapping = eForeignKey.getReferenceMapping().get(oldName);
                reAddFk.put(eForeignKey.getName(), refMapping);
                oldFkInfo.put(eForeignKey.getName(), eForeignKey.clone());
                new ForeignKeyEditorImpl(this.context, this.eTable, eForeignKey).removeColumn(new String[] { oldName });
            }
        });
        new ArrayList<>(this.eTable.getIndices()).forEach(eIndex -> {
            if (eIndex.getColumnList().contains(oldName)) {
                oldIdxInfo.put(eIndex.getName(), eIndex);
                reAddIndex.add(eIndex.getName());
                new IndexEditorImpl(this.context, this.eTable, eIndex).removeColumn(new String[] { oldName });
            }
        });
        if (this.eTable.getPrimaryKey() != null && this.eTable.getPrimaryKey().getColumnList().contains(oldName)) {
            new PrimaryEditorImpl(this.context, this.eTable, this.eTable.getPrimaryKey()).removeColumn(new String[] { oldName });
            reAddPk = true;
        }

        this.eColumn.setName(newName);

        if (reAddPk) {
            TableEditorImpl tableEditor = new TableEditorImpl(this.context, this.eTable);
            if (tableEditor.getPrimaryEditor() == null) {
                tableEditor.createPrimaryEditor(Collections.singletonList(newName));
            } else {
                tableEditor.getPrimaryEditor().addColumn(new String[] { newName });
            }
        }
        for (String indexName : reAddIndex) {
            IndexEditor indicesEditor = new TableEditorImpl(this.context, this.eTable).getIndexEditor(indexName);
            if (indicesEditor == null) {
                EIndex oldIndex = oldIdxInfo.get(indexName);
                IndexEditor newIdxEditor = new TableEditorImpl(this.context, this.eTable).addIndexEditor(indexName, oldIndex.getType(), Collections.singletonList(newName));
                newIdxEditor.fillAttr(oldIndex.getAttribute());
            } else {
                indicesEditor.addColumn(new String[] { newName });
            }
        }
        reAddFk.forEach((fkName, refColumn) -> {
            ForeignKeyEditor foreignKeyEditor = new TableEditorImpl(this.context, this.eTable).getForeignKeyEditor(fkName);
            if (foreignKeyEditor != null) {
                foreignKeyEditor.addColumn(newName, refColumn);
                return;
            }

            EForeignKey oldFk = oldFkInfo.get(fkName);
            Map<String, String> referenceMapping = new LinkedHashMap<>();
            referenceMapping.put(oldName, refColumn);
            new TableEditorImpl(this.context, this.eTable)
                .addForeignKeyEditor(fkName, oldFk.getReferenceSchema(), oldFk.getReferenceTable(), oldFk.getUpdateRule(), oldFk.getDeleteRule(), referenceMapping);
        });
    }

    @Override
    public void setComment(String comment) {
        if (Objects.equals(this.eColumn.getComment(), comment)) {
            return;
        }
        this.eColumn.setComment(comment);
    }

    @Override
    public void delete() {
        EColumn eColumn = findEColumn();
        if (eColumn == null) {
            return;
        }
        deleteKeyAndIndex();
        this.eTable.getColumnList().remove(eColumn);

    }

    private void deleteKeyAndIndex() {
        String columnName = this.eColumn.getName();
        new ArrayList<>(this.eTable.getForeignKeys()).forEach(eForeignKey -> {
            if (eForeignKey.getColumnList().contains(columnName)) {
                new ForeignKeyEditorImpl(this.context, this.eTable, eForeignKey).removeColumn(new String[] { columnName });
            }
        });
        new ArrayList<>(this.eTable.getIndices()).forEach(eIndex -> {
            if (eIndex.getColumnList().contains(columnName)) {
                new IndexEditorImpl(this.context, this.eTable, eIndex).removeColumn(new String[] { columnName });
            }
        });
        if (this.eTable.getPrimaryKey() != null && this.eTable.getPrimaryKey().getColumnList().contains(columnName)) {
            new PrimaryEditorImpl(this.context, this.eTable, this.eTable.getPrimaryKey()).removeColumn(new String[] { columnName });
        }
    }

    private EColumn findEColumn() {
        return this.eTable.getColumnList().stream().filter(eColumn -> eColumn.getName().equals(this.eColumn.getName())).findFirst().orElse(null);
    }

    private void diffChangeAdd(List<String> diffChange, boolean test, String key) {
        if (test && !diffChange.contains(key)) {
            diffChange.add(key);
        }
    }

    @Override
    public void change(String dbType, Boolean nullable, Long length, //
                       Integer numericPrecision, Integer numericScale, boolean unsigned, Integer datetimePrecision, //
                       String defaultValue, boolean defaultIsFunc, //
                       boolean autoGenerate, String comment) {
        boolean compareTypeDef = Objects.equals(dbType, this.eColumn.getDbType());
        boolean compareNullable = Objects.equals(nullable, this.eColumn.getNullable());
        boolean compareLength = Objects.equals(length, this.eColumn.getLength());
        boolean compareNumericPrecision = Objects.equals(numericPrecision, this.eColumn.getNumericPrecision());
        boolean compareNumericScale = Objects.equals(numericScale, this.eColumn.getNumericScale());
        boolean compareNumericUnsigned = Objects.equals(unsigned, this.eColumn.isNumericUnsigned());
        boolean compareDatetimePrecision = Objects.equals(datetimePrecision, this.eColumn.getDatetimePrecision());
        boolean compareDefaultValue = Objects.equals(defaultValue, this.eColumn.getDefaultValue());
        boolean compareDefaultValueIsFunc = Objects.equals(defaultIsFunc, this.eColumn.isDefaultValueIsFunc());
        boolean compareAutoGenerate = this.eColumn.isAutoGenerate() == autoGenerate;
        boolean compareComment = Objects.equals(comment, this.eColumn.getComment());

        List<String> diffChange = new ArrayList<>();
        diffChangeAdd(diffChange, compareTypeDef, "dbType");
        diffChangeAdd(diffChange, compareNullable, "nullable");
        diffChangeAdd(diffChange, compareLength, "length");
        diffChangeAdd(diffChange, compareNumericPrecision, "numericPrecision");
        diffChangeAdd(diffChange, compareNumericScale, "numericScale");
        diffChangeAdd(diffChange, compareNumericUnsigned, "numericUnsigned");
        diffChangeAdd(diffChange, compareDatetimePrecision, "datetimePrecision");
        diffChangeAdd(diffChange, compareDefaultValue, "defaultValue");
        diffChangeAdd(diffChange, compareDefaultValueIsFunc, "defaultIsFunc");
        diffChangeAdd(diffChange, compareAutoGenerate, "autoGenerate");
        diffChangeAdd(diffChange, compareComment, "comment");

        if (!diffChange.isEmpty()) {
            this.eColumn.setDbType(dbType);
            this.eColumn.setNullable(nullable);
            this.eColumn.setLength(length);
            this.eColumn.setNumericPrecision(numericPrecision);
            this.eColumn.setNumericScale(numericScale);
            this.eColumn.setNumericUnsigned(unsigned);
            this.eColumn.setDatetimePrecision(datetimePrecision);
            this.eColumn.setDefaultValue(defaultValue);
            this.eColumn.setDefaultValueIsFunc(defaultIsFunc);
            this.eColumn.setAutoGenerate(autoGenerate);
            this.eColumn.setComment(comment);
        }
    }

    @Override
    public void changeType(String dbType) {
        if (Objects.equals(this.eColumn.getDbType(), dbType)) {
            return;
        }

        this.change(dbType, this.eColumn.getNullable(), this.eColumn.getLength(), //
                this.eColumn.getNumericPrecision(), this.eColumn.getNumericScale(), this.eColumn.isNumericUnsigned(), this.eColumn.getDatetimePrecision(), //
                this.eColumn.getDefaultValue(), this.eColumn.isDefaultValueIsFunc(), //
                this.eColumn.isAutoGenerate(), this.eColumn.getComment());
    }

    @Override
    public void setNullable(Boolean isNullable) {
        if (Objects.equals(this.eColumn.getNullable(), isNullable)) {
            return;
        }

        this.change(this.eColumn.getDbType(), isNullable, this.eColumn.getLength(), //
                this.eColumn.getNumericPrecision(), this.eColumn.getNumericScale(), this.eColumn.isNumericUnsigned(), this.eColumn.getDatetimePrecision(), //
                this.eColumn.getDefaultValue(), this.eColumn.isDefaultValueIsFunc(), //
                this.eColumn.isAutoGenerate(), this.eColumn.getComment());
    }

    @Override
    public void setCharLength(Long length) {
        if (Objects.equals(length, this.eColumn.getLength())) {
            return;
        }

        this.change(this.eColumn.getDbType(), this.eColumn.getNullable(), length, //
                this.eColumn.getNumericPrecision(), this.eColumn.getNumericScale(), this.eColumn.isNumericUnsigned(), this.eColumn.getDatetimePrecision(), //
                this.eColumn.getDefaultValue(), this.eColumn.isDefaultValueIsFunc(), //
                this.eColumn.isAutoGenerate(), this.eColumn.getComment());
    }

    @Override
    public void setNumberLength(Integer numericPrecision, Integer numericScale) {
        if (Objects.equals(numericPrecision, this.eColumn.getNumericPrecision()) && Objects.equals(numericScale, this.eColumn.getNumericScale())) {
            return;
        }

        this.change(this.eColumn.getDbType(), this.eColumn.getNullable(), this.eColumn.getLength(), //
                numericPrecision, numericScale, this.eColumn.isNumericUnsigned(), this.eColumn.getDatetimePrecision(), //
                this.eColumn.getDefaultValue(), this.eColumn.isDefaultValueIsFunc(), //
                this.eColumn.isAutoGenerate(), this.eColumn.getComment());
    }

    @Override
    public void setUnsigned(boolean unsigned) {
        if (this.eColumn.isNumericUnsigned() == unsigned) {
            return;
        }

        this.change(this.eColumn.getDbType(), this.eColumn.getNullable(), this.eColumn.getLength(), //
                this.eColumn.getNumericPrecision(), this.eColumn.getNumericScale(), unsigned, this.eColumn.getDatetimePrecision(), //
                this.eColumn.getDefaultValue(), this.eColumn.isDefaultValueIsFunc(), //
                this.eColumn.isAutoGenerate(), this.eColumn.getComment());
    }

    @Override
    public void setDateTimePrecision(Integer datetimePrecision) {
        if (Objects.equals(datetimePrecision, this.eColumn.getDatetimePrecision())) {
            return;
        }

        this.change(this.eColumn.getDbType(), this.eColumn.getNullable(), this.eColumn.getLength(), //
                this.eColumn.getNumericPrecision(), this.eColumn.getNumericScale(), this.eColumn.isNumericUnsigned(), datetimePrecision, //
                this.eColumn.getDefaultValue(), this.eColumn.isDefaultValueIsFunc(), //
                this.eColumn.isAutoGenerate(), this.eColumn.getComment());
    }

    @Override
    public void setDefault(String defaultValue, boolean defaultIsFunc) {
        if (Objects.equals(this.eColumn.getDefaultValue(), defaultValue) && Objects.equals(this.eColumn.isDefaultValueIsFunc(), defaultIsFunc)) {
            return;
        }

        this.change(this.eColumn.getDbType(), this.eColumn.getNullable(), this.eColumn.getLength(), //
                this.eColumn.getNumericPrecision(), this.eColumn.getNumericScale(), this.eColumn.isNumericUnsigned(), this.eColumn.getDatetimePrecision(), //
                defaultValue, defaultIsFunc, //
                this.eColumn.isAutoGenerate(), this.eColumn.getComment());
    }

    @Override
    public void setAutoGenerate(boolean autoGenerate) {
        if (this.eColumn.isAutoGenerate() == autoGenerate) {
            return;
        }

        this.change(this.eColumn.getDbType(), this.eColumn.getNullable(), this.eColumn.getLength(), //
                this.eColumn.getNumericPrecision(), this.eColumn.getNumericScale(), this.eColumn.isNumericUnsigned(), this.eColumn.getDatetimePrecision(), //
                this.eColumn.getDefaultValue(), this.eColumn.isDefaultValueIsFunc(), //
                autoGenerate, this.eColumn.getComment());
    }

    @Override
    public void setColumnTypeWithDisplayWidth(String columnTypeWithDisplayWidth) {
        if (Objects.equals(this.eColumn.getColumnTypeWithDisplayWidth(), columnTypeWithDisplayWidth)) {
            return;
        }
        this.eColumn.setColumnTypeWithDisplayWidth(columnTypeWithDisplayWidth);
    }

    @Override
    public void setArrayDimension(Integer arrayDimension) {
        if (Objects.equals(arrayDimension, this.eColumn.getArrayDimension())) {
            return;
        }
        this.eColumn.setArrayDimension(arrayDimension);
    }
}
