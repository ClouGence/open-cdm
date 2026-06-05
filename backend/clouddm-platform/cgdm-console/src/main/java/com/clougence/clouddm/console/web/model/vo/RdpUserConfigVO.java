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
package com.clougence.clouddm.console.web.model.vo;

import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zylicfc
 */
@Getter
@Setter
public class RdpUserConfigVO {

    private String            uid;

    private String            configName;

    private String            configValue;

    private String            defaultValue;

    private String            valueRange;

    private String            description;

    private boolean           readOnly;

    private UserConfigTagType userConfigTagType;

    private String            i18nOfTagType;

    private String            confBelong;

    private KvConfValType     confValType;

    private boolean           isSecret;

    private boolean           needCreated;

    public void convertFromDO(DmSysUserConfDO config) {
        if (config.isSecret()) {
            this.setSecret(config.isSecret());
        } else {
            this.setConfigValue(config.getConfigValue());
        }

        this.setDescription(DmI18nUtils.getMessage(config.getDescKey()));
        this.setConfigName(config.getConfigName());
        this.setUserConfigTagType(config.getUserConfigTagType());
        if (config.getUserConfigTagType() != null) {
            this.i18nOfTagType = DmI18nUtils.getMessage("USER_CONFIG_TAG_" + config.getUserConfigTagType().name());
        }

        this.setConfBelong(config.getConfBelong().getCloudAliasName());
        this.setUid(config.getUid());
        this.setDefaultValue(config.getDefaultValue());
        this.setReadOnly(config.isReadOnly());
        this.setValueRange(config.getValueRange());

        if (config.getConfValType() != null) {
            this.setConfValType(config.getConfValType());
        } else {
            this.setConfValType(KvConfValType.TEXT);
        }
    }
}
