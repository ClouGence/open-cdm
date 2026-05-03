package com.clougence.clouddm.ds.mongodb.analysis;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.secrules.SecRulesSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

public class MongoSecRulesSupportSpi implements SecRulesSupportSpi {

    @Override
    public boolean isSupport() { return true; }

    @Override
    public List<TargetType> supportModel() {
        return Arrays.asList(       //
                TargetType.Schema,  //
                TargetType.Table,     //
                //
                TargetType.Query,   //
                TargetType.Update,  //
                TargetType.Delete,  //
                TargetType.Insert   //
        );
    }

    @Override
    public List<TargetType> exactRangeForQuery() {
        return Arrays.asList(TargetType.Schema, TargetType.Table);
    }

    @Override
    public List<TargetType> prefixRangeForQuery() {
        return Arrays.asList(TargetType.Schema, TargetType.Table);
    }

    @Override
    public List<TargetType> suffixRangeForQuery() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> includeRangeForQuery() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> exactRangeForSen() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> prefixRangeForSen() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> suffixRangeForSen() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> includeRangeForSen() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View);
    }
}
