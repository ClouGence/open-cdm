package com.clougence.clouddm.ds.secdomain.family.oracle;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraSecDomainResolve4IndexTest extends OraSecDomainTestSupport {

    public OraSecDomainResolve4IndexTest(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Oracle;
    }

    @Test
    public void createIndex_2() {
        String sql = "create index index_name on abc (id);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain1 = (RdbIndexDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_INDEX && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType().equals("index") && domain1.getName().equals("index_name") &&//
                   domain1.getTableName().equals("abc") && domain1.getColumns().contains("id");
        }
    }

    @Test
    public void createIndex_3() {
        String sql = "create index \"index_name\" on \"test\".\"abc\" (\"id\");";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain1 = (RdbIndexDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_INDEX && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema().equals("test") &&//
                   domain1.getType().equals("index") && domain1.getName().equals("index_name") &&//
                   domain1.getTableName().equals("abc") && domain1.getColumns().contains("id");
        }
    }

    @Test
    public void createUniqueIndex_1() {
        String sql = "create unique index index_name on abc (id);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain1 = (RdbIndexDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_INDEX && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType().equals("unique") && domain1.getName().equals("index_name") &&//
                   domain1.getTableName().equals("abc") && domain1.getColumns().contains("id");
        }
    }

}
