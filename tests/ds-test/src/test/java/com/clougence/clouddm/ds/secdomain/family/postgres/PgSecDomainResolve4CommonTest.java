package com.clougence.clouddm.ds.secdomain.family.postgres;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgSecDomainResolve4CommonTest extends PgSecDomainTestSupport {

    public PgSecDomainResolve4CommonTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PostgreSQL;
    }

    @Test
    public void commentTable_1() {
        String sql = "comment on table table_name is 'ss';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }

    @Test
    public void commentTable_2() {
        String sql = "comment on table schema1.table_name is 'ss';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/table_name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("schema1") && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }

    @Test
    public void commentTable_3() {
        String sql = "comment on table catalog1.schema1.table_name is 'ss';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/catalog1/schema1/table_name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("catalog1") && domain1.getSchema().equals("schema1") && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }

    @Test
    public void commentTable_4() {
        String sql = "comment on table \"catalog1\".\"schema1\".\"table_name\" is 'ss';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/catalog1/schema1/table_name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("catalog1") && domain1.getSchema().equals("schema1") && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }

    @Test
    public void commentColumn_1() {
        String sql = "comment on column table_name.column1 is 'ss';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_name/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_name/column1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_COLUMN;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgColumnDomain domain1 = (PgColumnDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_COLUMN && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }

    @Test
    public void commentColumn_2() {
        String sql = "comment on column schema1.table_name.column1 is 'ss';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/table_name/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/schema1/table_name/column1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_COLUMN;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgColumnDomain domain1 = (PgColumnDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_COLUMN && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("schema1") && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }

    @Test
    public void commentColumn_3() {
        String sql = "comment on column catalog1.schema1.table_name.column1 is 'ss';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/catalog1/schema1/table_name/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/catalog1/schema1/table_name/column1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_COLUMN;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgColumnDomain domain1 = (PgColumnDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_COLUMN && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("catalog1") && domain1.getSchema().equals("schema1") && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }

    @Test
    public void commentColumn_4() {
        String sql = "comment on column \"catalog1\".\"schema1\".\"table_name\".\"column1\" is 'ss';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/catalog1/schema1/table_name/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/catalog1/schema1/table_name/column1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_COLUMN;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgColumnDomain domain1 = (PgColumnDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_COLUMN && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("catalog1") && domain1.getSchema().equals("schema1") && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }
}
