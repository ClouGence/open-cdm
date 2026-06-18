package com.clougence.clouddm.ds.secdomain.family.mysql;// package com.clougence.clouddm.dsfamily.mysql.secdomain;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbResourceDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyFlushDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyFlushType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

// https://dev.mysql.com/doc/refman/8.4/en/show.html
public class MySecDomainResolve4OtherTest extends MySecDomainTestSupport {

    public MySecDomainResolve4OtherTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
    }

    //    @Test
    //    public void set_1() {
    //        List<RuleDomain> list = resolveSpi.resolveDomain(DataSourceType.MySQL, "set  binary log status");
    //    }
    //
    //    @Test
    //    public void flash_1() {
    //        List<RuleDomain> list = resolveSpi.resolveDomain(DataSourceType.MySQL, "set  binary log status");
    //    }

    @Test
    public void flushPrivileges() {
        String sql = "FLUSH PRIVILEGES;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.MYSQL_FLUSH;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyFlushDomain domain = (MyFlushDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.MYSQL_FLUSH && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getFlushType() == MyFlushType.PRIVILEGES;
        }
    }

    @Test
    public void flushLogs() {
        String sql = "FLUSH LOGS;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.MYSQL_FLUSH;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyFlushDomain domain = (MyFlushDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.MYSQL_FLUSH && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getFlushType() == MyFlushType.LOGS;
        }
    }

    @Test
    public void flushStatus() {
        String sql = "FLUSH status;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.MYSQL_FLUSH;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyFlushDomain domain = (MyFlushDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.MYSQL_FLUSH && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getFlushType() == MyFlushType.STATUS;
        }
    }

    @Test
    public void flushTables() {
        String sql = "FLUSH tables;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.MYSQL_FLUSH;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyFlushDomain domain = (MyFlushDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.MYSQL_FLUSH && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getFlushType() == MyFlushType.TABLES;
        }
    }

    @Test
    public void flushUserResource() {
        String sql = "FLUSH USER_RESOURCES;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.MYSQL_FLUSH;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyFlushDomain domain = (MyFlushDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.MYSQL_FLUSH && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getFlushType() == MyFlushType.USER_RESOURCE;
        }
    }

    @Test
    public void prepareStatement() {
        String sql = "PREPARE st1mt FROM 'SELECT * FROM test_users WHERE id = ?';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.PrepareStatement &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.PREPARE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbResourceDomain domain = (RdbResourceDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.PREPARE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getTarget() == TargetType.PrepareStatement;
        }
    }
    @Test
    public void executeStatement() {
        String sql = "execute stmt using @qq;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.PrepareStatement &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.EXECUTE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbResourceDomain domain = (RdbResourceDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.EXECUTE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getTarget() == TargetType.PrepareStatement;
        }
    }
    @Test
    public void transaction1() {
        String sql = "start transaction";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.TRANSACTION;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
    }

}
