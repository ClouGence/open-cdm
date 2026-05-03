package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.TruncateBuilder;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;

public class PgTruncateBuilder extends TruncateBuilder<PgTableDomain> {

    @Override
    protected PgTableDomain getTableDomain() { return new PgTableDomain(); }
}
