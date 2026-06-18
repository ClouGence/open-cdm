package com.clougence.clouddm.ds.secdomain.special.ob4ora;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.oracle.OraSecDomainResolve4IndexTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilySecDomainResolve4IndexTest extends OraSecDomainResolve4IndexTest {

    public OraFamilySecDomainResolve4IndexTest(){
        this.analysisSpi = new ObForOraResAnalysisSpi(null);
        this.resolveSpi = new ObForOraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObForOraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ObForOracle;
    }

}
