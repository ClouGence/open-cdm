package com.clougence.clouddm.ds.secdomain.family.starrocks;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.starrocks.analysis.SrResAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSecDomainResolveSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSplitAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrUserDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRoleDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class SrSecDomainResolve4AdministratorUserAndRoleTest extends SrSecDomainTestSupport {

    public SrSecDomainResolve4AdministratorUserAndRoleTest(){
        this.analysisSpi = new SrResAnalysisSpi(null);
        this.resolveSpi = new SrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.StarRocks;
    }

    @Test
    public void createUser() {
        String sql = "create user 'test_user'@'localhost' identified by 'test_password';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_USER;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrUserDomain domain = (SrUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_USER && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getHost().equals("localhost") && domain.getUser().equals("test_user") && domain.getPassword().equals("test_password");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createUserWithPassword() {
        String sql = "create user 'test_user'@'localhost'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_USER;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrUserDomain domain = (SrUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_USER && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getHost().equals("localhost") && domain.getUser().equals("test_user") && domain.getPassword() == null;
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createUser1() {
        String sql = "create user 'test_user' identified by 'test_password';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_USER;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrUserDomain domain = (SrUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_USER && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getHost().equals("%") && domain.getUser().equals("test_user") && domain.getPassword().equals("test_password");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createUser2() {
        String sql = "create user 'test_user'@'192.168.0.1' identified by 'test_password';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_USER;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrUserDomain domain = (SrUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_USER && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getHost().equals("192.168.0.1") && domain.getUser().equals("test_user") && domain.getPassword().equals("test_password");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void dropUser() {
        String sql = "drop user test_user@'localhost'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_USER;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrUserDomain domain = (SrUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_USER && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getHost().equals("localhost") && domain.getUser().equals("test_user") && domain.getPassword() == null;
            assert domain.getComment() == null;
        }
    }

    //    @Test
    //    public void rename_user() {
    //        String sql = "rename user 'test_user'@'localhost' to 'new_name'@'new_host';";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
    //        }
    //
    //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
    //        assert splitScripts.size() == 1;
    //        {
    //            assert splitScripts.get(0).getScript().equals(sql);
    //            assert splitScripts.get(0).getType() == SecQueryType.RENAME_USER;
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            SrUserDomain domain = (SrUserDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.RENAME_USER && domain.getSqlKind() == SqlKind.ALTER;
    //            assert domain.getHost().equals("localhost") && domain.getUser().equals("test_user") && domain.getPassword() == null;
    //            assert domain.getNewHost().equals("new_host") && domain.getNewName().equals("new_name");
    //            assert domain.getComment() == null;
    //        }
    //    }
    //
    //    @Test
    //    public void rename_user2() {
    //        String sql = "rename user 'test_user'@'localhost' to 'new_name';";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
    //        }
    //
    //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
    //        assert splitScripts.size() == 1;
    //        {
    //            assert splitScripts.get(0).getScript().equals(sql);
    //            assert splitScripts.get(0).getType() == SecQueryType.RENAME_USER;
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            SrUserDomain domain = (SrUserDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.RENAME_USER && domain.getSqlKind() == SqlKind.ALTER;
    //            assert domain.getHost().equals("localhost") && domain.getUser().equals("test_user") && domain.getPassword() == null;
    //            assert domain.getNewHost().equals("%") && domain.getNewName().equals("new_name");
    //            assert domain.getComment() == null;
    //        }
    //    }

    //    @Test
    //    public void grant() {
    //        String sql = "GRANT SELECT ON mydatabase.* TO 'test_user'@'localhost';";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
    //        }
    //
    //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
    //        assert splitScripts.size() == 1;
    //        {
    //            assert splitScripts.get(0).getScript().equals(sql);
    //            assert splitScripts.get(0).getType() == SecQueryType.GRANT;
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql));
    //        assert domainList.size() == 1;
    //        {
    //            DrGrantDomain domain = (DrGrantDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.GRANT && domain.getSqlKind() == SqlKind.ALTER;
    //            assert domain.getHost().equals("localhost") && domain.getName().equals("test_user");
    //        }
    //    }
    //
    //    @Test
    //    public void grantAll() {
    //        String sql = "GRANT ALL PRIVILEGES ON mydatabase.* TO 'test_user'@'localhost';";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
    //        }
    //
    //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
    //        assert splitScripts.size() == 1;
    //        {
    //            assert splitScripts.get(0).getScript().equals(sql);
    //            assert splitScripts.get(0).getType() == SecQueryType.GRANT;
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql));
    //        assert domainList.size() == 1;
    //        {
    //            DrGrantDomain domain = (DrGrantDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.GRANT && domain.getSqlKind() == SqlKind.ALTER;
    //            assert domain.getHost().equals("localhost") && domain.getName().equals("test_user");
    //        }
    //    }
    //
    //    @Test
    //    public void grantWithOption() {
    //        String sql = "GRANT SELECT ON mydatabase.* TO 'test_user'@'localhost' WITH GRANT OPTION;";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
    //        }
    //
    //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
    //        assert splitScripts.size() == 1;
    //        {
    //            assert splitScripts.get(0).getScript().equals(sql);
    //            assert splitScripts.get(0).getType() == SecQueryType.GRANT;
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql));
    //        assert domainList.size() == 1;
    //        {
    //            DrGrantDomain domain = (DrGrantDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.GRANT && domain.getSqlKind() == SqlKind.ALTER;
    //            assert domain.getHost().equals("localhost") && domain.getName().equals("test_user");
    //        }
    //    }
    //
    //    @Test
    //    public void revoke() {
    //        String sql = "REVOKE ALL PRIVILEGES ON mydatabase.* FROM 'test_user'@'localhost';";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
    //        }
    //
    //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
    //        assert splitScripts.size() == 1;
    //        {
    //            assert splitScripts.get(0).getScript().equals(sql);
    //            assert splitScripts.get(0).getType() == SecQueryType.REVOKE;
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql));
    //        assert domainList.size() == 1;
    //        {
    //            DrRevokeDomain domain = (DrRevokeDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.REVOKE && domain.getSqlKind() == SqlKind.ALTER;
    //            assert domain.getHost().equals("localhost") && domain.getName().equals("test_user");
    //        }
    //    }

    @Test
    public void createRole() {
        String sql = "create role test_role;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_role/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_ROLE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbRoleDomain domain = (RdbRoleDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_ROLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getRole().equals("test_role");
        }
    }

    @Test
    public void dropRole() {
        String sql = "drop role role1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/role1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_ROLE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbRoleDomain domain = (RdbRoleDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_ROLE && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getRole().equals("role1");
        }
    }

}
