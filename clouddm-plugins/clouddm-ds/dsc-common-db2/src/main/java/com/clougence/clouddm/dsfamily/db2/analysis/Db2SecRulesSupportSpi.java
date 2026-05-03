package com.clougence.clouddm.dsfamily.db2.analysis;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.secrules.SecRulesSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

public class Db2SecRulesSupportSpi implements SecRulesSupportSpi {

    @Override
    public boolean isSupport() { return true; }

    @Override
    public List<TargetType> supportModel() {
        return Arrays.asList(           //
                TargetType.Schema,      //
                TargetType.Table,       //
                TargetType.View,        //
                //TargetType.Materialized
                TargetType.Column,      //
                TargetType.Index,       //
                TargetType.Constraint,  //
                //                TargetType.Function,    //
                //                TargetType.Procedure,   //
                //                TargetType.Trigger,     //
                //TargetType.Event
                TargetType.Query,       //
                TargetType.Update,      //
                TargetType.Delete,      //
                TargetType.Insert//,      //
        //TargetType.Call        //
        );
    }

    @Override
    public List<TargetType> exactRangeForQuery() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View/*, TargetType.Column*/);
    }

    @Override
    public List<TargetType> prefixRangeForQuery() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View/*, TargetType.Column*/);
    }

    @Override
    public List<TargetType> suffixRangeForQuery() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View/*, TargetType.Column*/);
    }

    @Override
    public List<TargetType> includeRangeForQuery() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View/*, TargetType.Column*/);
    }

    @Override
    public List<TargetType> exactRangeForSen() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View, TargetType.Column);
    }

    @Override
    public List<TargetType> prefixRangeForSen() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View, TargetType.Column);
    }

    @Override
    public List<TargetType> suffixRangeForSen() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View, TargetType.Column);
    }

    @Override
    public List<TargetType> includeRangeForSen() {
        return Arrays.asList(TargetType.Schema, TargetType.Table, TargetType.View, TargetType.Column);
    }
}
