package com.clougence.schema.editor.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.clougence.schema.editor.ConflictException;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.TableEditor.ForeignKeyEditor;
import com.clougence.schema.editor.domain.EForeignKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/5/21 19:56
 */
class ForeignKeyEditorImpl extends AbstractEditor implements ForeignKeyEditor {

    private final ETable      eTable;
    private final EForeignKey eForeignKey;

    public ForeignKeyEditorImpl( EditorContext context, ETable eTable, EForeignKey eForeignKey){
        super(context);
        this.eTable = eTable;
        this.eForeignKey = eForeignKey;
    }

    @Override
    public EForeignKey getSource() { return this.eForeignKey; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }

    @Override
    public void rename(String newName) {
        if (StringUtils.isBlank(newName)) {
            throw new NullPointerException("new name is null.");
        }
        String oldName = this.eForeignKey.getName();
        if (StringUtils.equals(oldName, newName)) {
            return;
        }

        ForeignKeyEditor foreignKeyEditor = new TableEditorImpl(this.context, this.eTable).getForeignKeyEditor(newName);
        if (foreignKeyEditor != null) {
            throw new ConflictException("foreign Key '" + newName + " already exists.");
        }

        this.eForeignKey.setName(newName);
    }

    @Override
    public void addColumn(String columnName, String referenceColumn) {
        if (this.eForeignKey.getColumnList().contains(columnName)) {
            String refColumn = this.eForeignKey.getReferenceMapping().get(columnName);
            if (Objects.equals(refColumn, referenceColumn)) {
                return;
            } else {
                throw new ConflictException("foreign Key column '" + columnName + " have different references.");
            }
        }

        this.eForeignKey.getColumnList().add(columnName);
        this.eForeignKey.getReferenceMapping().put(columnName, referenceColumn);

    }

    @Override
    public void removeColumn(String[] columnName) {

        List<String> needRemove = new ArrayList<>();
        for (String column : columnName) {
            if (this.eForeignKey.getColumnList().contains(column)) {
                needRemove.add(column);
            }
        }

        if (needRemove.isEmpty()) {
            return;
        }

        this.eForeignKey.getColumnList().removeAll(needRemove);
        needRemove.forEach(key -> {
            this.eForeignKey.getReferenceMapping().remove(key);
        });

        if (this.eForeignKey.getColumnList().isEmpty()) {
            this.delete();
        }
    }

    @Override
    public void configUpdateRule(RdbForeignKeyRule foreignKeyRule) {

        foreignKeyRule = (foreignKeyRule == null) ? RdbForeignKeyRule.NoAction : foreignKeyRule;
        if (this.eForeignKey.getUpdateRule() == foreignKeyRule) {
            return;
        }
        this.eForeignKey.setUpdateRule(foreignKeyRule);

    }

    @Override
    public void configDeleteRule(RdbForeignKeyRule foreignKeyRule) {

        foreignKeyRule = (foreignKeyRule == null) ? RdbForeignKeyRule.NoAction : foreignKeyRule;
        if (this.eForeignKey.getDeleteRule() == foreignKeyRule) {
            return;
        }
        this.eForeignKey.setDeleteRule(foreignKeyRule);
    }

    public void delete() {
        EForeignKey oriForeignKey = this.eTable.getForeignKeys().stream().filter(foreignKey -> foreignKey.getName().equals(this.eForeignKey.getName())).findFirst().orElse(null);

        if (oriForeignKey != null) {
            this.eTable.getForeignKeys().remove(oriForeignKey);
        }
    }
}
