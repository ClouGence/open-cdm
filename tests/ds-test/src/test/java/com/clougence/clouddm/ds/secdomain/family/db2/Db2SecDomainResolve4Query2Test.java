package com.clougence.clouddm.ds.secdomain.family.db2;

import org.junit.Test;

import com.clougence.clouddm.dsfamily.db2.analysis.Db2ResAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2SecDomainResolve4Query2Test extends Db2SecDomainTestSupport {

    public Db2SecDomainResolve4Query2Test(){
        this.analysisSpi = new Db2ResAnalysisSpi();
        this.resolveSpi = new Db2SecDomainResolveSpi();
        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Db2;
    }

    @Test
    public void mixWithQuery_1() {
        //        String sql = "WITH updated_data AS (\n" +//
        //                     "    UPDATE pub.sch.table1\n" +//
        //                     "        SET column1 = 1\n" +//
        //                     "        WHERE condition = 1\n" +//
        //                     "        RETURNING id\n" +//
        //                     ") " +//
        //                     "select * from pub.sch.table2 where id in (select id from updated_data);";
        //
        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 2;
        //        {
        //            assert resList.get(0).getType() == TargetType.Table &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_table/");
        //            assert resList.get(1).getType() == TargetType.Column &&//
        //                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/user_table/address/");
        //        }
        //
        //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 2;
        //        {
        //            assert domainList.get(0) instanceof RdbSelectDomain;
        //            assert domainList.get(1) instanceof PgUpdateDomain;
        //            RdbSelectDomain selectDomain = (RdbSelectDomain) domainList.get(0);
        //            PgUpdateDomain updateDomain = (PgUpdateDomain) domainList.get(1);
        //            assert selectDomain.getCatalog().equals("pub") && selectDomain.getSchema().equals("sch") && selectDomain.getTable().equals("table2");
        //            assert updateDomain.getCatalog().equals("pub") && updateDomain.getSchema().equals("sch") && updateDomain.getTable().equals("table1");
        //        }
    }

    @Test
    public void mixWithQuery_2() {
        //        String sql = "WITH updated_data AS (\n" +//
        //                     "    UPDATE table1\n" +//
        //                     "        SET column1 = 1\n" +//
        //                     "        WHERE condition = 1\n" +//
        //                     "        RETURNING id\n" +//
        //                     "), delete_data AS (\n" +//
        //                     "    delete from table1\n" +//
        //                     "    WHERE condition = 1\n" +//
        //                     "    RETURNING id\n" +//
        //                     "    )\n" +//
        //                     "select * from table2 where id in (select id from updated_data)\n" +//
        //                     "union all\n" +//
        //                     "select * from table2 where id in (select id from delete_data);";
        //
        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 2;
        //        {
        //            assert resList.get(0).getType() == TargetType.Table &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_table/");
        //            assert resList.get(1).getType() == TargetType.Column &&//
        //                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/user_table/address/");
        //        }
        //
        //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 3;
    }
}
