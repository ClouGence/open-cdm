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
package com.clougence.clouddm.console.web.service.system;

import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.mapper.system.DmSysUserConfMapper;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InstallationReportState {

    public static final String  INSTALL = "install";
    public static final String  UPGRADE = "upgrade";

    @Resource
    private DmSysUserConfMapper userConfMapper;

    @Transactional(rollbackFor = Throwable.class)
    public String reportIfNecessary(String version, Predicate<String> reporter) {
        DmSysUserConfDO configDO = this.userConfMapper.queryByUidAndConfigNameForUpdate(AuthDal.ROOT_USER_UID, RootUserConfig.Fields.installReport);
        if (configDO == null) {
            log.warn("Skip installation report because its KV state does not exist, version={}", version);
            return null;
        }

        ReportConfig reportConfig = resolveConfig(version, configDO);
        if (!reportConfig.equals(parseConfigValue(configDO.getConfigValue()))) {
            this.userConfMapper.updateConfigValueById(configDO.getId(), toConfigValue(reportConfig));
        }
        if (reportConfig.isReported()) {
            return null;
        }
        if (!reporter.test(reportConfig.getType())) {
            return null;
        }

        reportConfig.setReported(true);
        if (this.userConfMapper.updateConfigValueById(configDO.getId(), toConfigValue(reportConfig)) != 1) {
            throw new IllegalStateException("Failed to mark installation report as reported: " + version);
        }
        return reportConfig.getType();
    }

    public static ReportConfig pendingConfig(String version, String type) {
        if (!isSupportedType(type)) {
            throw new IllegalArgumentException("Unsupported installation report type: " + type);
        }
        return new ReportConfig(version, type, false);
    }

    public static ReportConfig parseConfigValue(String configValue) {
        try {
            return JsonUtils.toObj(configValue, ReportConfig.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toConfigValue(ReportConfig reportConfig) {
        return JsonUtils.toJson(reportConfig);
    }

    private static ReportConfig resolveConfig(String version, DmSysUserConfDO configDO) {
        ReportConfig reportConfig = configDO == null ? null : parseConfigValue(configDO.getConfigValue());
        if (reportConfig == null || !version.equals(reportConfig.getVersion()) || !isSupportedType(reportConfig.getType())) {
            return pendingConfig(version, UPGRADE);
        }
        return reportConfig;
    }

    private static boolean isSupportedType(String type) {
        return INSTALL.equals(type) || UPGRADE.equals(type);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportConfig {

        private String  version;
        private String  type;
        private boolean reported;
    }
}
