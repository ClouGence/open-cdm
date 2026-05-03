package com.clougence.clouddm.sec.rules;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.sdk.service.secrules.CheckerData;
import com.clougence.clouddm.sdk.service.secrules.CheckerRange;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sec.rules.execute.Utils;
import com.clougence.utils.JsonUtils;

public class RangeTestCase extends AbstractRangeTestCase {

    @Test
    public void envRangeTest_1() {
        String json = "{'scope':'Environment','matchMode':'EXACT','levelPrefix':'/','levelNodes':['1'],'chooseAll':false}";
        String sql = "select * from users;";

        {
            CheckerRange range = JsonUtils.toObj(json.replace("'", "\""), CheckerRange.class);
            List<RuleDomain> domainList = configDsAndEnv(1, 1, this.resolveSpi.resolveDomain(dataSourceType, sql, baseLine, baseColumn));
            assert domainList.size() == 1;
            assert Utils.checkRangeIncludeDomain(range, new CheckerData(sql, domainList.get(0)));
        }
        {
            CheckerRange range = JsonUtils.toObj(json.replace("'", "\""), CheckerRange.class);
            List<RuleDomain> domainList = configDsAndEnv(1, 2, this.resolveSpi.resolveDomain(dataSourceType, sql, baseLine, baseColumn));
            assert domainList.size() == 1;
            assert Utils.checkRangeIncludeDomain(range, new CheckerData(sql, domainList.get(0)));
        }
        {
            CheckerRange range = JsonUtils.toObj(json.replace("'", "\""), CheckerRange.class);
            List<RuleDomain> domainList = configDsAndEnv(2, 1, this.resolveSpi.resolveDomain(dataSourceType, sql, baseLine, baseColumn));
            assert domainList.size() == 1;
            assert !Utils.checkRangeIncludeDomain(range, new CheckerData(sql, domainList.get(0)));
        }
    }

    @Test
    public void envRangeTest_2() {
        String json = "{'scope':'Environment','matchMode':'EXACT','levelPrefix':'/','levelNodes':['1'],'chooseAll':false}";
        String sql = "insert into users (id,name) values(1,'222');";

        {
            CheckerRange range = JsonUtils.toObj(json.replace("'", "\""), CheckerRange.class);
            List<RuleDomain> domainList = configDsAndEnv(1, 1, this.resolveSpi.resolveDomain(dataSourceType, sql, baseLine, baseColumn));
            assert domainList.size() == 1;
            assert Utils.checkRangeIncludeDomain(range, new CheckerData(sql, domainList.get(0)));
        }
        {
            CheckerRange range = JsonUtils.toObj(json.replace("'", "\""), CheckerRange.class);
            List<RuleDomain> domainList = configDsAndEnv(1, 2, this.resolveSpi.resolveDomain(dataSourceType, sql, baseLine, baseColumn));
            assert domainList.size() == 1;
            assert Utils.checkRangeIncludeDomain(range, new CheckerData(sql, domainList.get(0)));
        }
        {
            CheckerRange range = JsonUtils.toObj(json.replace("'", "\""), CheckerRange.class);
            List<RuleDomain> domainList = configDsAndEnv(2, 1, this.resolveSpi.resolveDomain(dataSourceType, sql, baseLine, baseColumn));
            assert domainList.size() == 1;
            assert !Utils.checkRangeIncludeDomain(range, new CheckerData(sql, domainList.get(0)));
        }
    }
}
