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

import com.clougence.clouddm.base.metadata.ds.ConfigValType;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;

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
    private ConfigValType     confValType;
    private boolean           isSecret;
    private boolean           needCreated;
}
