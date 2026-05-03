package com.clougence.clouddm.ds.oracle.analysis.builder;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraColumnDomain;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CommentBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

public class OraCommentBuilder extends CommentBuilder {

    public OraCommentBuilder(TargetType targetType){
        super(targetType);
    }

    @Override
    protected RdbColumnDomain getColumnDomain() { return new OraColumnDomain(); }

    @Override
    protected RdbTableDomain getTableDomain() { return new OraTableDomain(); }
}
