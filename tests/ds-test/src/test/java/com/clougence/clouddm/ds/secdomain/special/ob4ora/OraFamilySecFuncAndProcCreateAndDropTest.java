package com.clougence.clouddm.ds.secdomain.special.ob4ora;

import com.clougence.clouddm.ds.secdomain.family.oracle.OraSecFuncAndProcCreateAndDropTest;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.sql.oracle.resource.OraResAnalysisSpi;
import com.clougence.sql.oracle.security.OraSecDomainResolveSpi;
import com.clougence.sql.oracle.split.OraSplitAnalysisSpi;

public class OraFamilySecFuncAndProcCreateAndDropTest extends OraSecFuncAndProcCreateAndDropTest {

    public OraFamilySecFuncAndProcCreateAndDropTest(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ObForOracle;
    }

}
