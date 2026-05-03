package com.clougence.clouddm.ds.secdomain.special.tidb;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.ds.tidb.analysis.TiResAnalysisSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class TiSecDomainResolve4ConstraintTest extends MySecDomainTestSupport {

    public TiSecDomainResolve4ConstraintTest(){
        this.analysisSpi = new TiResAnalysisSpi(null);
        this.resolveSpi = new TiSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new TiSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.TiDB;
    }

    @Test
    public void primaryKeyAdd_1() {
        String sql1 = "ALTER TABLE t1 ADD KEY www (c1(33),c2(33)) comment 'wegwgwgwgwe';";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        String sql2 = "ALTER TABLE t1 ADD PRIMARY KEY kkk (c1,c2);";
        List<RuleDomain> domainList = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void primaryKeyAdd_2() {
        String sql1 = "ALTER TABLE t1 ADD KEY www (c1(33),c2(33)) comment 'wegwgwgwgwe';";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        String sql2 = "ALTER TABLE t1 ADD PRIMARY KEY kkk (c1,c2) comment '999';";
        List<RuleDomain> domainList = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getComment().equals("999");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void uniqueKeyAdd_1() {
        String sql = "ALTER TABLE t1 ADD UNIQUE KEY kkk (c1,c2);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void uniqueKeyAdd_2() {
        String sql = "ALTER TABLE t1 ADD UNIQUE KEY kkk (c1,c2) comment '999';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getComment().equals("999");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void uniqueKeyAdd_3() {
        String sql = "ALTER TABLE t1 ADD UNIQUE INDEX kkk (c1,c2);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void uniqueKeyAdd_4() {
        String sql = "ALTER TABLE t1 ADD UNIQUE INDEX kkk (c1,c2) comment '999';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getComment().equals("999");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }
}
