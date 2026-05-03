package com.clougence.clouddm.ds.secdomain.special.redis;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.redis.analysis.RedisResAnalysisSpi;
import com.clougence.clouddm.ds.redis.analysis.RedisSecDomainResolveSpi;
import com.clougence.clouddm.ds.redis.analysis.RedisSplitAnalysisSpi;
import com.clougence.clouddm.ds.redis.analysis.secrules.RedisCmdDomain;
import com.clougence.clouddm.ds.secdomain.family.redis.RedisSecDomainTestSupport;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class RedisDomainResolve4CommandTest extends RedisSecDomainTestSupport {

    public RedisDomainResolve4CommandTest(){
        this.analysisSpi = new RedisResAnalysisSpi(null);
        this.resolveSpi = new RedisSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new RedisSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Redis;
    }

    @Test
    public void commands_1() {
        String sql = "set abc 123 \n get abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 2;
        {
            assert splitScripts.get(0).getScript().equals("set abc 123");
            assert splitScripts.get(0).getType() == SecQueryType.WRITE;
        }
        {
            assert splitScripts.get(1).getScript().equals("get abc");
            assert splitScripts.get(1).getType() == SecQueryType.READ;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RedisCmdDomain domain = (RedisCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.WRITE && domain.getAuditKind() == SecQueryKind.WRITE;
            assert domain.getSchema() == null && domain.getCommand().equals("SET");
        }
        {
            RedisCmdDomain domain = (RedisCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.READ && domain.getAuditKind() == SecQueryKind.READ;
            assert domain.getSchema() == null && domain.getCommand().equals("GET");
        }
    }
}
