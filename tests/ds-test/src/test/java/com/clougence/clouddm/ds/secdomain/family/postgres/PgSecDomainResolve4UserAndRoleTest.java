package com.clougence.clouddm.ds.secdomain.family.postgres;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbGrantDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRevokeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRoleDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUserDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgRoleDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgSecDomainResolve4UserAndRoleTest extends PgSecDomainTestSupport {

    public PgSecDomainResolve4UserAndRoleTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PostgreSQL;
    }

    @Test
    public void createUser() {
        String sql = "CREATE USER test_user WITH PASSWORD 'test_password';";

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
    public void dropUser1() {
        String sql = "DROP USER test_user;";

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
    public void dropUserIfExists() {
        String sql = "DROP USER if exists test_user;";

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
            assert domain.getComment() == null && domain.isIfExists();
        }
    }

    @Test
    public void dropUserMulti() {
        String sql = "DROP USER test_user,test_user2;";

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
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
            assert resList.get(1).getType() == TargetType.UserOrRole &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_user2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_USER;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbUserDomain domain = (RdbUserDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_USER && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getUser().equals("test_user") && domain.getPassword() == null;
            assert domain.getComment() == null;
        }
        {
            RdbUserDomain domain = (RdbUserDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.DROP_USER && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getUser().equals("test_user2") && domain.getPassword() == null;
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createRole1() {
        String sql = "create role test_role;";

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
            PgRoleDomain domain = (PgRoleDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_ROLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getRole().equals("test_role");
            assert domain.getPassword() == null;
        }
    }

    //    @Test
    //    public void createRole2() {
    //        String sql = "CREATE ROLE app_user WITH LOGIN PASSWORD 'secure_password';";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_role/");
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            PgRoleDomain domain = (PgRoleDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.CREATE_ROLE && domain.getSqlKind() == SqlKind.CREATE;
    //            assert domain.getRole().equals("test_role");
    //            assert domain.getPassword() == null;
    //        }
    //    }

    @Test
    public void dropRole1() {
        String sql = "DROP ROLE test_role;";

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
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_role/");
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
            PgRoleDomain domain = (PgRoleDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_ROLE && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getRole().equals("test_role");
        }
    }
    //
    //    @Test
    //    public void dropRoleIfExists() {
    //        String sql = "DROP ROLE if exists test_role;";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_role/");
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            PgRoleDomain domain = (PgRoleDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.DROP_ROLE && domain.getSqlKind() == SqlKind.DROP;
    //            assert domain.getRole().equals("test_role");
    //        }
    //    }

    @Test
    public void grantToUser() {
        String sql = "GRANT SELECT, INSERT ON TABLE employees TO test_user;";

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
    public void grantDatabaseToUser() {
        String sql = "GRANT SELECT, INSERT ON DATABASE employees TO test_user;";

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
    public void grantToUserMulti() {
        String sql = "GRANT SELECT, INSERT ON TABLE employees TO test_user,test_user2;";

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
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
            assert resList.get(1).getType() == TargetType.UserOrRole &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_user2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.GRANT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbGrantDomain domain = (RdbGrantDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.GRANT && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user");
        }
        {
            RdbGrantDomain domain = (RdbGrantDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.GRANT && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user2");
        }
    }
    //
    //    @Test
    //    public void grantAll() {
    //        String sql = "GRANT ALL PRIVILEGES ON mydatabase.* TO 'test_user'@'localhost' IDENTIFIED BY 'mypassword';";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.UserOrRole &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
    //        }
    //
    //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 2;
    //        {
    //            MyUserDomain domain = (MyUserDomain) domainList.get(0);
    //            assert domain.getSqlType() == SecQueryType.CREATE_USER && domain.getSqlKind() == SqlKind.CREATE;
    //            assert domain.getHost().equals("localhost") && domain.getUser().equals("test_user") && domain.getPassword().equals("mypassword");
    //            assert domain.getNewHost() == null && domain.getNewName() == null;
    //            assert domain.getComment() == null;
    //        }
    //        {
    //            MyGrantDomain domain = (MyGrantDomain) domainList.get(1);
    //            assert domain.getSqlType() == SecQueryType.GRANT && domain.getSqlKind() == SqlKind.ALTER;
    //            assert domain.getHost().equals("localhost") && domain.getName().equals("test_user");
    //        }
    //    }

    @Test
    public void revoke() {
        String sql = "REVOKE INSERT ON TABLE employees FROM test_user;";

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
    public void revokeMulti() {
        String sql = "REVOKE INSERT ON TABLE employees FROM test_user,test_user2;";

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
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_user/");
        }
        {
            assert resList.get(1).getType() == TargetType.UserOrRole &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_user2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.REVOKE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbRevokeDomain domain = (RdbRevokeDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.REVOKE && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user");
        }
        {
            RdbRevokeDomain domain = (RdbRevokeDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.REVOKE && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("test_user2");
        }
    }

    @Test
    public void createRole() {
        String sql = "create role test_role;";

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

    //    @Test
    //    public void grantRole() {
    //        String sql = "GRANT manager TO app_user;";
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
