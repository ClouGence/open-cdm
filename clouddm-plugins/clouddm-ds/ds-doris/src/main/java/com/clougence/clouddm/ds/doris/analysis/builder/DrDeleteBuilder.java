package com.clougence.clouddm.ds.doris.analysis.builder;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrDeleteDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DeleteDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbDeleteDomain;

public class DrDeleteBuilder extends DeleteDomainBuilder {

    @Override
    protected RdbDeleteDomain getDeleteDomain() { return new DrDeleteDomain(); }

}
