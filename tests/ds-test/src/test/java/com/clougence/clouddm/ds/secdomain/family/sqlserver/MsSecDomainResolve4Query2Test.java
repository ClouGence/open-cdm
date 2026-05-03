package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4Query2Test extends MsSecDomainTestSupport {

    public MsSecDomainResolve4Query2Test(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void mixWithQuery_1() {
        String sql = "SELECT AddressLine1,\n" + "    AddressLine2,\n" + "    City,\n" + "    PostalCode,\n" + "    CountryRegionCode\n" + "FROM Person.Address AS a\n"
                     + "INNER JOIN Person.StateProvince AS s\n" + "    ON a.StateProvinceID = s.StateProvinceID\n" + "WHERE CountryRegionCode NOT IN ('US')\n"
                     + "    AND City LIKE N'Pa%';";
        //
        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/Person/Address/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/Person/StateProvince/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }
        //
        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 5 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.JOIN;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("Person") && domain1.getTable().equals("Address") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void mixWithQuery_2() {
        String sql = "-- Define the CTE expression name and column list.\n" + "WITH Sales_CTE (IFD, NAME)\n" + "AS\n" + "-- Define the CTE query.\n" + "(\n"
                     + "    SELECT ID, NAME\n" + "    FROM user_table\n" + "    WHERE ID IS NOT NULL\n" + ")\n" + "-- Define the outer query referencing the CTE name.\n"
                     + "SELECT IFD, NAME\n" + "FROM Sales_CTE";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/");
            //                    assert resList.get(1).getType() == TargetType.Column &&//
            //                           resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/address/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL && domain1.getSqlType() == SecQueryType.SELECT;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectVariables().size() == 0//
                   && domain1.getSelectFunc().size() == 0 && domain1.getWhereColumns().size() == 0;// ;
            assert domain1.getSelectColumns().contains("IFD") && domain1.getSelectColumns().contains("NAME");
            assert domain1.isVirtual() && domain1.getTable().equals("Sales_CTE");

        }
    }
}
