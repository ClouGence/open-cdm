package com.clougence.schema.editor.builder;

import com.clougence.schema.editor.ConflictException;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ConstraintEditor;
import com.clougence.schema.editor.domain.EConstraint;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

import java.util.Map;

public class ConstraintEditorImpl extends AbstractEditor implements ConstraintEditor {

    private final ETable      eTable;
    private final EConstraint eConstraint;

    ConstraintEditorImpl(EditorContext context, ETable eTable, EConstraint eConstraint){
        super(context);
        this.eTable = eTable;
        this.eConstraint = eConstraint;
    }

    @Override
    public void rename(String newName) {
        if (StringUtils.isBlank(newName)) {
            throw new NullPointerException("new name is null.");
        }
        String oldName = this.eConstraint.getName();
        if (StringUtils.equals(oldName, newName)) {
            return;
        }

        this.eConstraint.setName(newName);
    }

    @Override
    public EConstraint getSource() { return this.eConstraint; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }
}
