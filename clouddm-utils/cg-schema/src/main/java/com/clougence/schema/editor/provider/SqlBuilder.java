package com.clougence.schema.editor.provider;

import com.clougence.schema.DsType;
import com.clougence.schema.editor.builder.mappings.DefaultMappingHandler;
import com.clougence.schema.editor.builder.mappings.MappingHandler;
import com.clougence.schema.editor.triggers.*;
import com.clougence.schema.metadata.MainVersion;

public interface SqlBuilder extends TableTriggers, ColumnTriggers, IndexTriggers, PrimaryKeyTriggers, ForeignKeyTriggers, ConstraintTriggers {

    DsType getDataSourceType();

    default MappingHandler createHandler(DsType tarDsType, MainVersion mainVersion) {
        return new DefaultMappingHandler(this.getDataSourceType(), tarDsType, mainVersion);
    }
}
