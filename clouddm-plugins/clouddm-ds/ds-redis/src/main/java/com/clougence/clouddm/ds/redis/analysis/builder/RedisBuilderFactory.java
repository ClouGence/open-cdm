package com.clougence.clouddm.ds.redis.analysis.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.factory.SimpleBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class RedisBuilderFactory extends SimpleBuilderFactory {

    public RedisBuilderFactory(MetaService metaService){
        super(metaService);
    }
}
