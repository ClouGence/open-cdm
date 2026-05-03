package com.clougence.schema.editor.builder.actions;

import java.util.List;

import com.clougence.schema.editor.builder.DsInfo;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;

@FunctionalInterface
public interface ExecuteGenerateSql {

    List<String> execute(DsInfo dsInfo, TriggerContext triggerContext, ETable eTable, EColumn eColumn);
}
