package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.AggregateFunc;

public class DbAggregateFunc extends AggregateFunc {

    @Override
    protected String getQuoteName() { return "1"; }
}
