package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AlterSchemaBuilder;

public class McAlterSchemaBuilder extends AlterSchemaBuilder<McSchemaDomain> {

    @Override
    protected McSchemaDomain getSchemaDomain() { return new McSchemaDomain(); }

}
