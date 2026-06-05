/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
