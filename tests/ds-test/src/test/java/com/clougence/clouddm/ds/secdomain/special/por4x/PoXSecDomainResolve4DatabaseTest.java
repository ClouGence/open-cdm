package com.clougence.clouddm.ds.secdomain.special.por4x;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.polardb.analysis.porx.PorXResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySchemaDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PoXSecDomainResolve4DatabaseTest extends MySecDomainTestSupport {

    public PoXSecDomainResolve4DatabaseTest(){
        this.analysisSpi = new PorXResAnalysisSpi(null);
        this.resolveSpi = new PorXSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDbX;
    }

    //@Test
    public void createDatabase_1() {
        //        List<RuleDomain> list = resolveSpi.resolveDomain(this.dataSourceType, "CREATE DATABASE db1 PARTITION_MODE=sharding LOCALITY='dn=polardbx-storage-0-master';");
        //        RdbSchemaDomain domain = (RdbSchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getSqlKind() == SqlKind.CREATE;
        //
        //        assert domain.getCatalog().equals("def") && domain.getSchema() == null && domain.getOptions().isEmpty();
    }

    @Test
    public void createDatabase_2() {
        String sql = "CREATE DATABASE db1 default CHARACTER SET = UTF8;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/db1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("db1") && domain.getOptions().isEmpty();
            assert domain.getCharacterSet().equals("UTF8");
        }
    }

    //@Test
    public void createDatabase_3() {
        //        list = resolveSpi.resolveDomain(this.dataSourceType, "CREATE DATABASE db1 PARTITION_MODE = 'sharding';");
        //         domain = (RdbSchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.CREATE_CATALOG && domain.getSqlKind() == SqlKind.CREATE;
        //
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("db1") && domain.getOptions().isEmpty();
    }
}
