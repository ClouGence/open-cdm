package com.clougence.schema.editor.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.schema.editor.ConflictException;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.NoColumnException;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.INDEX_CONTAINS_EXPRESSION;

/**
 * @author mode 2021/5/21 19:56
 */
class IndexEditorImpl extends AbstractEditor implements IndexEditor {

    private final ETable eTable;
    private final EIndex eIndex;

    public IndexEditorImpl(EditorContext context, ETable eTable, EIndex eIndex){
        super(context);
        this.eTable = eTable;
        this.eIndex = eIndex;
    }

    @Override
    public EIndex getSource() { return this.eIndex; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }

    @Override
    public void rename(String newName) {
        if (StringUtils.isBlank(newName)) {
            throw new NullPointerException("new name is null.");
        }
        String oldName = this.eIndex.getName();
        if (StringUtils.equals(oldName, newName)) {
            return;
        }

        IndexEditor indexEditor = new TableEditorImpl(this.context, this.eTable).getIndexEditor(newName);
        if (indexEditor != null) {
            throw new ConflictException("index '" + newName + " already exists.");
        }

        this.eIndex.setName(newName);
    }

    @Override
    public void addColumn(String[] columnName) {
        List<String> needAdd = new ArrayList<>();
        for (String column : columnName) {
            if (!this.eIndex.getColumnList().contains(column)) {
                needAdd.add(column);
            }
        }

        if (needAdd.isEmpty()) {
            return;
        }

        if (!Boolean.parseBoolean(this.getAttr(INDEX_CONTAINS_EXPRESSION.getCodeKey()))) {
            List<String> tableAllColumns = this.eTable.getColumnList().stream().map(EColumn::getName).collect(Collectors.toList());
            for (String column : needAdd) {
                if (!tableAllColumns.contains(column)) {
                    throw new NoColumnException("column '" + column + "' not found.");
                }
            }
        }

        this.eIndex.getColumnList().addAll(needAdd);
    }

    @Override
    public void removeColumn(String[] columnName) {
        List<String> needRemove = new ArrayList<>();
        for (String column : columnName) {
            if (this.eIndex.getColumnList().contains(column)) {
                needRemove.add(column);
            }
        }

        if (needRemove.isEmpty()) {
            return;
        }

        this.eIndex.getColumnList().removeAll(needRemove);
        if (this.eIndex.getColumnList().isEmpty()) {
            this.delete();
        }
    }

    @Override
    public void delete() {
        EIndex oriIndex = this.eTable.getIndices().stream().filter(index -> index.getName().equals(eIndex.getName())).findFirst().orElse(null);

        if (oriIndex != null) {
            this.eTable.getIndices().remove(oriIndex);
        }
    }

    @Override
    public void setComment(String newComment) {

        String oldComment = this.eIndex.getComment();
        if (StringUtils.equals(oldComment, newComment)) {
            return;
        }

        this.eIndex.setComment(newComment);
    }
}
