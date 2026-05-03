package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CommentBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

public class PgCommentBuilder extends CommentBuilder {

    public PgCommentBuilder(TargetType targetType){
        super(targetType);
    }

    @Override
    protected RdbTableDomain getTableDomain() { return new PgTableDomain(); }

    @Override
    protected RdbColumnDomain getColumnDomain() { return new PgColumnDomain(); }
}
