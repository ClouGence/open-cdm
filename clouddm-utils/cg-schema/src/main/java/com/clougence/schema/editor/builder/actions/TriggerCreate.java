package com.clougence.schema.editor.builder.actions;

import com.clougence.schema.editor.builder.DsInfo;
import com.clougence.schema.editor.domain.ETable;

@FunctionalInterface
public interface TriggerCreate {

    void triggerCreate(DsInfo srcInfo, DsInfo tarInfo, ETable table);
}
