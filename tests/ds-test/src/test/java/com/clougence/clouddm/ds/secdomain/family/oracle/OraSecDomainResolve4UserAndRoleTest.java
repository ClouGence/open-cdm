package com.clougence.clouddm.ds.secdomain.family.oracle;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbGrantDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRevokeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRoleDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUserDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraSecDomainResolve4UserAndRoleTest extends OraSecDomainTestSupport {

    public OraSecDomainResolve4UserAndRoleTest(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Oracle;
    }

    @Test
    public void createUser() {
        String sql = "create user test_user identified by \"test_password\";";

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
            RdbUserDomain domain = (RdbUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_USER && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getUser().equals("test_user") && domain.getPassword().equals("test_password");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createUserWithPassword() {
        String sql = "create user \"test_user\" identified by test_password;";

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
            RdbUserDomain domain = (RdbUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_USER && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getUser().equals("test_user") && domain.getPassword().equals("test_password");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void dropUser() {
        String sql = "drop user test_user";

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
            RdbUserDomain domain = (RdbUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_USER && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getUser().equals("test_user") && domain.getPassword() == null;
            assert domain.getComment() == null;
        }
    }

    @Test
    public void dropUser_2() {
        String sql = "drop user \"test_user\"";

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
            RdbUserDomain domain = (RdbUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_USER && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getUser().equals("test_user") && domain.getPassword() == null;
            assert domain.getComment() == null;
        }
    }

    @Test
    public void grant() {
        String sql = "GRANT SELECT ON mydatabase.test TO test_user;";

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
            assert splitScripts.get(0).getType() == SecQueryType.GRANT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbGrantDomain domain = (RdbGrantDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.GRANT && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user");
        }
    }

    @Test
    public void grant_1() {
        String sql = "GRANT SELECT ON mydatabase.test TO \"test_user\";";

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
            assert splitScripts.get(0).getType() == SecQueryType.GRANT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbGrantDomain domain = (RdbGrantDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.GRANT && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user");
        }
    }

    @Test
    public void grantAll() {
        String sql = "GRANT ALL PRIVILEGES TO test_user;";

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
            assert splitScripts.get(0).getType() == SecQueryType.GRANT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbGrantDomain domain = (RdbGrantDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.GRANT && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user");
        }
    }

    @Test
    public void grantWithOption() {
        String sql = "GRANT SELECT ON mydatabase.test_table TO test_user WITH GRANT OPTION;";

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
            assert splitScripts.get(0).getType() == SecQueryType.GRANT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbGrantDomain domain = (RdbGrantDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.GRANT && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user");
        }
    }

    @Test
    public void revoke() {
        String sql = "REVOKE ALL PRIVILEGES ON mydatabase FROM test_user;";

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
            assert splitScripts.get(0).getType() == SecQueryType.REVOKE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbRevokeDomain domain = (RdbRevokeDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.REVOKE && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user");
        }
    }

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
    public void createRole_1() {
        String sql = "create role \"test_role\";";

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

    //    @Test
    //    public void dropRole() {
    //        String sql = "drop role role1;";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/role1/");
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            RdbRoleDomain domain = (RdbRoleDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.DROP_ROLE && domain.getSqlKind() == SqlKind.DROP;
    //            assert domain.getRole().equals("role1");
    //        }
    //    }

}
