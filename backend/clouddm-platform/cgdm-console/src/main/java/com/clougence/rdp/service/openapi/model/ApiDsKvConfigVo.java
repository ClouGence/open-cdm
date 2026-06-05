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
package com.clougence.rdp.service.openapi.model;

import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.clouddm.console.web.model.vo.RdpDsKvConfigVO;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: baili
 * @Date: 2023/04/13/19:57
 * @Description:
 */
@Getter
@Setter
public class ApiDsKvConfigVo {

    private Long          id;

    private String        configName;

    private DsConfigGroup configGroup;

    private String        description;

    private boolean       valueRequire;

    private String        valueValidRegex;

    private String        configValue;

    private String        defaultValue;

    private String        valueAdvance;

    private boolean       readOnly;

    private boolean       isSecret;

    private boolean       needCreated;

    public void convertFromDsKvConfigVO(RdpDsKvConfigVO configVO) {
        this.id = configVO.getId();
        this.configName = configVO.getConfigName();
        this.configGroup = configVO.getConfigGroup();
        this.description = configVO.getDescription();
        this.valueRequire = configVO.isValueRequire();
        this.valueValidRegex = configVO.getValueValidRegex();
        this.configValue = configVO.getConfigValue();
        this.defaultValue = configVO.getDefaultValue();
        this.valueAdvance = configVO.getValueAdvance();
        this.readOnly = configVO.isReadOnly();
        this.isSecret = configVO.isSecret();
        this.needCreated = configVO.isNeedCreated();
    }
}
