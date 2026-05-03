package com.clougence.clouddm.ds.ads.analysis.ads4my;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySecRulesSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

public class AdsMySecRulesSupportSpi extends MySecRulesSupportSpi {

    @Override
    public List<TargetType> supportModel() {
        return Arrays.asList(       //
                TargetType.Schema,  //
                TargetType.Table,   //
                TargetType.View,    //
                TargetType.Column,  //
                TargetType.Query,   //
                TargetType.Update,  //
                TargetType.Delete,  //
                TargetType.Insert);
    }
}
