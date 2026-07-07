package com.clougence.clouddm.ds.secdomain.special.ob4ora;

import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.resource.ObForOraResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.security.ObForOraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.split.ObForOraSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.oracle.OraSecDomainResolve4UserAndRoleTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraFamilySecDomainResolve4UserAndRoleTest extends OraSecDomainResolve4UserAndRoleTest {

    public OraFamilySecDomainResolve4UserAndRoleTest(){
        this.analysisSpi = new ObForOraResAnalysisSpi(null);
        this.resolveSpi = new ObForOraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObForOraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ObForOracle;
    }

}
