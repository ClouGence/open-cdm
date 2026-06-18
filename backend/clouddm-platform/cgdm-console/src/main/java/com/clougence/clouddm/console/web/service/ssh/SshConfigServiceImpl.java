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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.session.ssh.SshRService;
import com.clougence.clouddm.api.sidecar.session.ssh.TestResultDTO;
import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ssh.SshConfigSaveFO;
import com.clougence.clouddm.console.web.model.fo.ssh.SshProxyFeaturesFO;
import com.clougence.clouddm.console.web.model.fo.ssh.TestSshConnectionFO;
import com.clougence.clouddm.console.web.model.vo.ssh.SshConfigDetailVO;
import com.clougence.clouddm.console.web.model.vo.ssh.SshConfigListVO;
import com.clougence.clouddm.console.web.util.CallUtils;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmSshConfigDO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SshConfigServiceImpl implements SshConfigService {

    private record TestSshConfigDTO(String workerSeqNumber, SshConfig sshConfig, boolean strictHostKeyChecking) {
    }

    @Resource
    private SystemDal   systemDal;
    @Resource
    private SshRService sshRService;

    @Override
    public List<SshConfigListVO> list(String search) {
        return this.systemDal.sshConfigMapper()//
            .queryList(search)
            .stream()
            .map(DmConvertUtils::convertToSshConfigListVO)
            .collect(Collectors.toList());
    }

    @Override
    public SshConfigDetailVO detail(long id) {
        DmSshConfigDO configDO = this.requireConfig(id);
        return DmConvertUtils.convertToSshConfigDetailVO(configDO);
    }

    @Override
    public Long create(String uid, SshConfigSaveFO fo) {
        validate(fo);

        DmSshConfigDO configDO = buildStorageConfig(null, fo);
        configDO.setGmtCreate(new Date());
        configDO.setGmtModified(configDO.getGmtCreate());
        this.systemDal.sshConfigMapper().insert(configDO);
        return configDO.getId();
    }

    @Override
    public void update(String uid, SshConfigSaveFO fo) {
        if (fo.getId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_ID_REQUIRED_ERROR.name()));
        }

        validate(fo);
        DmSshConfigDO exists = requireConfig(fo.getId());
        DmSshConfigDO configDO = buildStorageConfig(exists, fo);
        configDO.setId(exists.getId());
        configDO.setGmtModified(new Date());
        this.systemDal.sshConfigMapper().updateById(configDO);
    }

    private void validate(SshConfigSaveFO fo) {
        if (fo == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_REQUIRED_ERROR.name()));
        }
        if (StringUtils.isBlank(fo.getName())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_NAME_BLANK_ERROR.name()));
        }
        if (StringUtils.isBlank(fo.getHost())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_HOST_BLANK_ERROR.name()));
        }
        if (StringUtils.isBlank(fo.getUsername())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_USERNAME_BLANK_ERROR.name()));
        }
        if (fo.getAuthType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_AUTH_TYPE_REQUIRED_ERROR.name()));
        }
    }

    @Override
    public void delete(String uid, Long id) {
        if (id == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_ID_REQUIRED_ERROR.name()));
        }

        this.systemDal.sshConfigMapper().deleteById(id);
    }

    @Override
    public TestResultDTO testConnection(TestSshConnectionFO fo) {
        if (fo == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_TEST_REQUEST_REQUIRED_ERROR.name()));
        }

        TestSshConfigDTO testConfig = prepareTestConfig(fo);
        String workerSeqNumber = testConfig.workerSeqNumber();
        SshConfig sshConfig = testConfig.sshConfig();
        boolean strictHostKeyChecking = testConfig.strictHostKeyChecking();

        // test connection
        long start = System.currentTimeMillis();
        log.info("start test ssh connection, sshConfigId={}, workerSeqNumber={}, host={}, port={}, username={}, authType={}, proxyType={}, strictHostKeyChecking={}",//
                fo.getSshConfigId(), workerSeqNumber, sshConfig.getHost(), sshConfig.getPort(), sshConfig.getUsername(),//
                sshConfig.getAuthType(), sshConfig.getProxyType(), strictHostKeyChecking);
        try {
            TestResultDTO result = this.sshRService.testConnection(CallUtils.buildSendDTO(workerSeqNumber), sshConfig);
            if (Boolean.TRUE.equals(result.getSuccess())) {
                log.info("finish test ssh connection, sshConfigId={}, workerSeqNumber={}, host={}, port={}, success=true, costMs={}",//
                        fo.getSshConfigId(), workerSeqNumber, sshConfig.getHost(), sshConfig.getPort(), result.getCostMs());
            } else {
                log.warn("finish test ssh connection, sshConfigId={}, workerSeqNumber={}, host={}, port={}, success=false, costMs={}, message={}",//
                        fo.getSshConfigId(), workerSeqNumber, sshConfig.getHost(), sshConfig.getPort(), result.getCostMs(), result.getMessage());
            }
            return result;
        } catch (RuntimeException e) {
            log.warn("test ssh connection request failed, sshConfigId={}, workerSeqNumber={}, host={}, port={}, costMs={}",//
                    fo.getSshConfigId(), workerSeqNumber, sshConfig.getHost(), sshConfig.getPort(), System.currentTimeMillis() - start, e);
            throw e;
        }
    }

    private TestSshConfigDTO prepareTestConfig(TestSshConnectionFO fo) {
        String workerSeqNumber;
        if (StringUtils.isNotBlank(fo.getWorkerSeqNumber())) {
            workerSeqNumber = fo.getWorkerSeqNumber();
        } else if (fo.getConfig() != null) {
            workerSeqNumber = fo.getConfig().getWorkerSeqNumber();
        } else {
            workerSeqNumber = null;
        }

        DmSshConfigDO exists = requireConfig(fo.getSshConfigId());
        if (fo.getConfig() == null && exists == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_REQUIRED_ERROR.name()));
        }

        SshConfig sshConfig = fo.getConfig() == null ? DmConvertUtils.convertToSshConfig(exists) : DmConvertUtils.convertToSshConfigForTest(exists, fo.getConfig());
        SshConFeatures conFeatures = sshConfig.getConFeatures();
        boolean strictHostKeyChecking = false;
        if (conFeatures != null && conFeatures.getHostKey() != null) {
            strictHostKeyChecking = conFeatures.getHostKey().isStrictChecking();
        } else if (conFeatures != null) {
            strictHostKeyChecking = conFeatures.isStrictHostKeyChecking();
        }

        if (fo.getConfig() == null && strictHostKeyChecking && (conFeatures == null || conFeatures.getKnownHosts() == null || conFeatures.getKnownHosts().isEmpty())) {
            if (conFeatures == null) {
                conFeatures = new SshConFeatures();
                sshConfig.setConFeatures(conFeatures);
                exists.setConFeatures(conFeatures);
            }
            log.info("auto probe and save missing ssh known hosts before test, sshConfigId={}, workerSeqNumber={}, host={}, port={}",//
                    fo.getSshConfigId(), workerSeqNumber, sshConfig.getHost(), sshConfig.getPort());
            conFeatures.setKnownHosts(this.sshRService.probeKnownHosts(CallUtils.buildSendDTO(workerSeqNumber), sshConfig));
            exists.setGmtModified(new Date());
            this.systemDal.sshConfigMapper().updateById(exists);
        }

        return new TestSshConfigDTO(workerSeqNumber, sshConfig, strictHostKeyChecking);
    }

    @Override
    public List<SshKnownHost> probeKnownHosts(TestSshConnectionFO fo) {
        if (fo == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_TEST_REQUEST_REQUIRED_ERROR.name()));
        }

        String workerSeqNumber;
        if (StringUtils.isNotBlank(fo.getWorkerSeqNumber())) {
            workerSeqNumber = fo.getWorkerSeqNumber();
        } else if (fo.getConfig() != null) {
            workerSeqNumber = fo.getConfig().getWorkerSeqNumber();
        } else {
            workerSeqNumber = null;
        }

        DmSshConfigDO exists = requireConfig(fo.getSshConfigId());
        if (fo.getConfig() == null && exists == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_REQUIRED_ERROR.name()));
        }

        SshConfig sshConfig = fo.getConfig() == null ? DmConvertUtils.convertToSshConfig(exists) : DmConvertUtils.convertToSshConfigForTest(exists, fo.getConfig());
        log.info("probe ssh known hosts, sshConfigId={}, workerSeqNumber={}, host={}, port={}",//
                fo.getSshConfigId(), workerSeqNumber, sshConfig.getHost(), sshConfig.getPort());
        return this.sshRService.probeKnownHosts(CallUtils.buildSendDTO(workerSeqNumber), sshConfig);
    }

    //

    private DmSshConfigDO requireConfig(Long id) {
        if (id == null) {
            return null;
        }

        DmSshConfigDO configDO = this.systemDal.sshConfigMapper().queryById(id);
        if (configDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_NOT_EXIST_ERROR.name(), id));
        }
        return configDO;
    }

    @Override
    public SshConfig fetchSshConfig(Long id) {
        if (id == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_ID_REQUIRED_ERROR.name()));
        } else {
            return DmConvertUtils.convertToSshConfig(requireConfig(id));
        }
    }

    private DmSshConfigDO buildStorageConfig(DmSshConfigDO exists, SshConfigSaveFO fo) {
        DmSshConfigDO configDO = new DmSshConfigDO();
        configDO.setName(fo.getName());
        configDO.setHost(fo.getHost());
        configDO.setPort(fo.getPort());
        configDO.setUsername(fo.getUsername());
        configDO.setAuthType(fo.getAuthType());
        if (fo.getPassword() == null && exists != null) {
            configDO.setPassword(exists.getPassword());
        } else {
            configDO.setPassword(fo.getPassword());
        }
        if (fo.getPrivateKeyData() == null && exists != null) {
            configDO.setPrivateKeyData(exists.getPrivateKeyData());
        } else {
            configDO.setPrivateKeyData(fo.getPrivateKeyData());
        }
        if (fo.getPrivateKeyPassphrase() == null && exists != null) {
            configDO.setPrivateKeyPassphrase(exists.getPrivateKeyPassphrase());
        } else {
            configDO.setPrivateKeyPassphrase(fo.getPrivateKeyPassphrase());
        }

        // SshConFeatures
        SshConFeatures existsConFeatures = exists == null ? null : exists.getConFeatures();
        SshConFeatures conFeatures = fo.getConFeatures() == null ? existsConFeatures : fo.getConFeatures();
        if (conFeatures == null) {
            conFeatures = new SshConFeatures();
        }
        if ((conFeatures.getKnownHosts() == null || conFeatures.getKnownHosts().isEmpty()) && existsConFeatures != null) {
            conFeatures.setKnownHosts(existsConFeatures.getKnownHosts());
        }
        configDO.setConFeatures(conFeatures);

        // SshProxyFeatures
        SshProxyType proxyType = fo.getProxyType() == null ? SshProxyType.NO_PROXY : fo.getProxyType();
        SshProxyFeatures proxyFeatures;
        if (proxyType == SshProxyType.NO_PROXY) {
            proxyFeatures = new SshProxyFeatures();
        } else {
            proxyFeatures = buildStorageProxyFeatures(fo.getProxyFeatures(), exists == null ? null : exists.getProxyFeatures());
        }
        configDO.setProxyType(proxyType);
        configDO.setProxyFeatures(proxyFeatures);
        boolean strictHostKeyChecking = conFeatures.isStrictHostKeyChecking();
        if (conFeatures.getHostKey() != null) {
            strictHostKeyChecking = conFeatures.getHostKey().isStrictChecking();
        }
        if (strictHostKeyChecking && (conFeatures.getKnownHosts() == null || conFeatures.getKnownHosts().isEmpty())) {
            SshConfig runtime = DmConvertUtils.convertToSshConfig(configDO);
            log.info("auto probe missing ssh known hosts before save, sshConfigId={}, workerSeqNumber={}, host={}, port={}",//
                    fo.getId(), fo.getWorkerSeqNumber(), runtime.getHost(), runtime.getPort());
            conFeatures.setKnownHosts(this.sshRService.probeKnownHosts(CallUtils.buildSendDTO(fo.getWorkerSeqNumber()), runtime));
        }

        return configDO;
    }

    private SshProxyFeatures buildStorageProxyFeatures(SshProxyFeaturesFO submitted, SshProxyFeatures exists) {
        if (submitted == null) {
            return exists == null ? new SshProxyFeatures() : exists;
        }

        SshProxyFeatures features = new SshProxyFeatures();
        features.setHost(submitted.getHost());
        features.setPort(submitted.getPort());
        features.setSecurityType(submitted.getSecurityType());
        if (submitted.getSecurityType() == SecurityType.USER_PASSWD) {
            features.setUsername(submitted.getUsername());
            if (submitted.getPassword() == null && exists != null) {
                features.setPassword(exists.getPassword());
            } else {
                features.setPassword(submitted.getPassword());
            }
        }
        return features;
    }

}
