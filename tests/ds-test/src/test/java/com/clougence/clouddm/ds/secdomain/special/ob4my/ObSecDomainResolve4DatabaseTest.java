package com.clougence.clouddm.ds.secdomain.special.ob4my;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySchemaDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ObSecDomainResolve4DatabaseTest extends MySecDomainTestSupport {

    public ObSecDomainResolve4DatabaseTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }

    //    @Test
    public void alertDataBase_0() {
        String sql1 = "alter database abc default character set utf8mb4;";
        List<RuleDomain> list = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getAuditKind() == SecQueryKind.ALTER;
        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        assert domain.getCollate() == null && domain.getCharacterSet().equals("utf8mb4");

        String sql2 = "alter database abc character set = utf8mb4;";
        list = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql2), contextInfo());
        domain = (MySchemaDomain) list.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getAuditKind() == SecQueryKind.ALTER;
        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        assert domain.getCollate() == null && domain.getCharacterSet().equals("utf8mb4");

        String sql3 = "alter database abc character set utf8mb4;";
        list = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql3), contextInfo());
        domain = (MySchemaDomain) list.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getAuditKind() == SecQueryKind.ALTER;
        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        assert domain.getCollate() == null && domain.getCharacterSet().equals("utf8mb4");

        //todo can't parse
        //        list = resolveSpi.resolveDomain(sourceType, "alter database character set utf8mb4;");
        //        domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate() == null && domain.getCharacterSet().equals("utf8mb4");

        // todo can't parse
        //        list = resolveSpi.resolveDomain(sourceType, "alter database abc charset utf8mb4;");
        //        domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate() == null && domain.getCharacterSet().equals("utf8mb4");
    }

    @Test
    public void alertDataBase_1() {
        // can't parse
        //        List<RuleDomain> list = resolveSpi.resolveDomain(sourceType, "alter schema abc default character set utf8mb4 default collate utf8mb4_unicode_ci;");
        //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
        //        List<RuleDomain> list = resolveSpi.resolveDomain(sourceType, "alter schema abc default character set utf8mb4 collate = utf8mb4_unicode_ci;");
        //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
        //
        //        List<RuleDomain> list = resolveSpi.resolveDomain(sourceType, "alter schema abc default collate  utf8mb4_unicode_ci;");
        //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
    }

    // todo can't parse
    @Test
    public void alertDataBase_2() {
        //        List<RuleDomain> list = resolveSpi.resolveDomain(sourceType, "alter schema abc read only");
        //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");

        //        List<RuleDomain> list = resolveSpi.resolveDomain(sourceType, "alter schema abc read write");
        //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
    }

    @Test
    public void alertDataBase_3() {
        //        List<RuleDomain> list = resolveSpi.resolveDomain(sourceType, "alter schema abc default table group null");
        //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");

        //        List<RuleDomain> list = resolveSpi.resolveDomain(sourceType, "alter schema abc read write");
        //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
    }

    //    @Test
    //    public void createDatabase_1() {
    //        List<RuleDomain> list = resolveSpi.resolveDomain(dataSourceType, "create schema if not exists test1 character set utf8mb4;");
    //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
    //        assert domain.getSqlType() == SecQueryType.CREATE_CATALOG && domain.getSqlKind() == SqlKind.CREATE;
    //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("test1");
    //        assert domain.getCharacterSet().equals("utf8mb4");
    //
    //        // todo can't parse
    //        //        list = resolveSpi.resolveDomain(sourceType, "create database test read only");
    //        //         domain = (MySchemaDomain) list.get(0);
    //        //        assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getSqlKind() == SqlKind.ALTER;
    //        //        assert domain.getCatalog().equals("def") && domain.getSchema().equals("abc");
    //        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
    //    }

}
