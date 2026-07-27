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
package com.clougence.clouddm.console.web.service.cicd;

import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.console.web.model.fo.cicd.GuideCheckFlowFO;

public class DmChangeFlowServiceImplHashTest {

    @Test
    public void shouldIsolateRepositoryIdsByScmConfiguration() {
        DmChangeFlowServiceImpl service = new DmChangeFlowServiceImpl();
        GuideCheckFlowFO firstInstance = flow(11);
        GuideCheckFlowFO secondInstance = flow(22);

        assertNotEquals(service.toHash(firstInstance), service.toHash(secondInstance));
    }

    private static GuideCheckFlowFO flow(long scmId) {
        GuideCheckFlowFO flow = new GuideCheckFlowFO();
        flow.setRepoScmId(scmId);
        flow.setRepoScmUrl("https://gitlab.example/gitlab/group/database");
        flow.setRepoId("1");
        flow.setRepoBranch("main");
        flow.setDsId("mysql-test");
        flow.setDsLevels(List.of("mysql-test", "database"));
        return flow;
    }
}
