package com.clougence.schema.editor.triggers;

import com.clougence.schema.editor.domain.EConstraint;

import java.util.Collections;
import java.util.List;

public interface ConstraintTriggers extends java.util.EventListener {

    default List<String> createConstraint(TriggerContext buildContext, String catalog, String schema, String table, //
                                          EConstraint constraint) {
        return Collections.emptyList();
    }

    default List<String> dropConstraint(TriggerContext buildContext, String catalog, String schema, String table, //
                                        EConstraint constraint) {
        return Collections.emptyList();
    }

}
