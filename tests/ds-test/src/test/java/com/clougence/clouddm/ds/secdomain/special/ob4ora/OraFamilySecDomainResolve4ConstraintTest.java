package com.clougence.clouddm.ds.secdomain.special.ob4ora;

import com.clougence.clouddm.ds.oceanbase.analysis.obforora.ObForOraResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.obforora.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.obforora.ObForOraSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.oracle.OraSecDomainResolve4ConstraintTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilySecDomainResolve4ConstraintTest extends OraSecDomainResolve4ConstraintTest {

    public OraFamilySecDomainResolve4ConstraintTest(){
        this.analysisSpi = new ObForOraResAnalysisSpi(null);
        this.resolveSpi = new ObForOraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObForOraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ObForOracle;
    }

}
