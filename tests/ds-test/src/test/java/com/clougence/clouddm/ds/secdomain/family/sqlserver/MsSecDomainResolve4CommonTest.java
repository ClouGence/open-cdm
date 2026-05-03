package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4CommonTest extends MsSecDomainTestSupport {

    public MsSecDomainResolve4CommonTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

//    @Test
//    public void commentTable_1() {
//        String sql = "comment on table table_name is 'ss';";
//
//        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
//        assert resList.size() == 1;
//        {
//            assert resList.get(0).getType() == TargetType.Table &&//
//                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_name/");
//        }
//
//        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
//        assert domainList.size() == 1;
//        {
//            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
//            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getSqlKind() == SqlKind.ALTER;
//            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_name") &&//
//                   domain1.getComment().equals("ss");
//        }
//    }
//
//    @Test
//    public void commentColumn_1() {
//        String sql = "comment on column table_name.column1 is 'ss';";
//
//        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
//        assert resList.size() == 2;
//        {
//            assert resList.get(0).getType() == TargetType.Table &&//
//                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_name/");
//            assert resList.get(1).getType() == TargetType.Column &&//
//                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_name/column1/");
//        }
//
//        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
//        assert domainList.size() == 1;
//        {
//            PgColumnDomain domain1 = (PgColumnDomain) domainList.get(0);
//            assert domain1.getSqlType() == SecQueryType.COMMENT_COLUMN && domain1.getSqlKind() == SqlKind.ALTER;
//            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_name") &&//
//                   domain1.getComment().equals("ss");
//        }
//    }
}
