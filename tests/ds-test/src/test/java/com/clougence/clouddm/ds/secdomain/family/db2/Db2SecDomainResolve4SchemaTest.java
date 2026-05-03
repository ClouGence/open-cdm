package com.clougence.clouddm.ds.secdomain.family.db2;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.db2.analysis.Db2ResAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2ColumnDomain;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2SchemaDomain;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2TableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2SecDomainResolve4SchemaTest extends Db2SecDomainTestSupport {

    public Db2SecDomainResolve4SchemaTest(){
        this.analysisSpi = new Db2ResAnalysisSpi();
        this.resolveSpi = new Db2SecDomainResolveSpi();
        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Db2;
    }

    @Test
    public void createSchema_1() {
        String sql = "create schema abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        //        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SchemaDomain domain1 = (Db2SchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_includeElement_1() {
        String sql = "create schema abc create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/abc/test_user/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/abc/test_user/id/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/abc/test_user/name/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        //        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            Db2SchemaDomain domain1 = (Db2SchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") && domain1.getOptions().isEmpty();
        }
        {
            Db2TableDomain domain = (Db2TableDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") &&//
                   domain.getOptions().isEmpty() &&//
                   domain.getColumns().size() == 2;
        }
        {
            Db2ColumnDomain domain = (Db2ColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") && domain.getColumn().equals("id");//

        }
    }

    @Test
    public void dropSchema_1() {
        String sql = "drop schema abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }
        //
        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        //        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SchemaDomain domain1 = (Db2SchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") && !domain1.isIfExists();
        }
    }

    @Test
    public void dropSchema_2() {
        String sql = "drop schema abc cascade";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        //        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SchemaDomain domain2 = (Db2SchemaDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.DROP_SCHEMA && domain2.getAuditKind() == SecQueryKind.DROP;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("abc") && !domain2.isIfExists();
        }
    }

    @Test
    public void dropSchema_3() {
        String sql = "drop schema abc restrict";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        //        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SchemaDomain domain3 = (Db2SchemaDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.DROP_SCHEMA && domain3.getAuditKind() == SecQueryKind.DROP;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("abc") && !domain3.isIfExists();
        }
    }

    @Test
    public void dropSchema_4() {
        String sql = "drop schema if exists abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        //        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SchemaDomain domain4 = (Db2SchemaDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.DROP_SCHEMA && domain4.getAuditKind() == SecQueryKind.DROP;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("abc") && domain4.isIfExists();
        }
    }

    @Test
    public void dropSchema_5() {
        String sql = "drop schema if exists abc cascade";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        //        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            Db2SchemaDomain domain5 = (Db2SchemaDomain) domainList.get(0);
            assert domain5.getSqlType() == SecQueryType.DROP_SCHEMA && domain5.getAuditKind() == SecQueryKind.DROP;
            assert domain5.getCatalog() == null && domain5.getSchema().equals("abc") && domain5.isIfExists();
        }
    }

    @Test
    public void dropSchema_6() {
        String sql = "drop schema if exists abc restrict";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }
        //
        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        //        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            Db2SchemaDomain domain6 = (Db2SchemaDomain) domainList.get(0);
            assert domain6.getSqlType() == SecQueryType.DROP_SCHEMA && domain6.getAuditKind() == SecQueryKind.DROP;
            assert domain6.getCatalog() == null && domain6.getSchema().equals("abc") && domain6.isIfExists();
        }
    }
}
