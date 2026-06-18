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
package com.clougence.clouddm.console.web.service.ssh;

import java.util.List;

import com.clougence.clouddm.api.sidecar.session.ssh.TestResultDTO;
import com.clougence.clouddm.base.metadata.ds.SshConfig;
import com.clougence.clouddm.base.metadata.ds.SshKnownHost;
import com.clougence.clouddm.console.web.model.fo.ssh.SshConfigSaveFO;
import com.clougence.clouddm.console.web.model.fo.ssh.TestSshConnectionFO;
import com.clougence.clouddm.console.web.model.vo.ssh.SshConfigDetailVO;
import com.clougence.clouddm.console.web.model.vo.ssh.SshConfigListVO;

public interface SshConfigService {

    List<SshConfigListVO> list(String search);

    SshConfigDetailVO detail(long id);

    Long create(String uid, SshConfigSaveFO fo);

    void update(String uid, SshConfigSaveFO fo);

    void delete(String uid, Long id);

    TestResultDTO testConnection(TestSshConnectionFO fo);

    List<SshKnownHost> probeKnownHosts(TestSshConnectionFO fo);

    SshConfig fetchSshConfig(Long id);
}
