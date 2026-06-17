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

@Service
public class SshConfigServiceImpl implements SshConfigService {
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
        if (fo.getConfig() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CONFIG_REQUIRED_ERROR.name()));
        }

        String workerSeqNumber;
        if (StringUtils.isNotBlank(fo.getWorkerSeqNumber())) {
            workerSeqNumber = fo.getWorkerSeqNumber();
        } else {
            workerSeqNumber = fo.getConfig().getWorkerSeqNumber();
        }

        DmSshConfigDO exists = requireConfig(fo.getSshConfigId());
        SshConfig sshConfig = DmConvertUtils.convertToSshConfigForTest(exists, fo.getConfig());
        return this.sshRService.testConnection(CallUtils.buildSendDTO(workerSeqNumber), sshConfig);
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
        configDO.setPort(fo.getPort() == null ? 22 : fo.getPort());
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

        // 
        boolean strictHostKeyChecking = conFeatures.isStrictHostKeyChecking();
        if (conFeatures.getHostKey() != null) {
            strictHostKeyChecking = conFeatures.getHostKey().isStrictChecking();
        }
        if (strictHostKeyChecking) {
            SshConfig runtime = DmConvertUtils.convertToSshConfig(configDO);
            List<SshKnownHost> knownHosts = this.sshRService.probeKnownHosts(CallUtils.buildSendDTO(fo.getWorkerSeqNumber()), runtime);
            conFeatures.setKnownHosts(knownHosts);
        }
        return configDO;
    }

    private SshProxyFeatures buildStorageProxyFeatures(SshProxyFeaturesFO submitted, SshProxyFeatures exists) {
        if (submitted == null) {
            if (exists != null) {
                return exists;
            }
            SshProxyFeatures features = new SshProxyFeatures();
            if (features.getSecurityType() == null) {
                features.setSecurityType(StringUtils.isNotBlank(features.getPassword()) ? SecurityType.USER_PASSWD : SecurityType.ONLY_USER);
            }
            return features;
        }

        SshProxyFeatures features = new SshProxyFeatures();
        features.setHost(submitted.getHost());
        features.setPort(submitted.getPort());
        SecurityType securityType = submitted.getSecurityType();
        if (securityType == null) {
            securityType = StringUtils.isNotBlank(submitted.getPassword()) ? SecurityType.USER_PASSWD : SecurityType.ONLY_USER;
        }

        features.setSecurityType(securityType);
        features.setUsername(submitted.getUsername());
        if (securityType == SecurityType.USER_PASSWD) {
            if (submitted.getPassword() == null && exists != null) {
                features.setPassword(exists.getPassword());
            } else {
                features.setPassword(submitted.getPassword());
            }
        }
        return features;
    }
}
