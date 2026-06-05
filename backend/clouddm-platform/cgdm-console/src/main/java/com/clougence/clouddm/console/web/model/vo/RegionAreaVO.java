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

import com.clougence.clouddm.console.web.constants.RegionArea;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2023/4/14 22:23:48
 */
@Getter
@Setter
public class RegionAreaVO {

    private RegionArea regionArea;

    private String     i18nName;

    public RegionAreaVO(RegionArea regionArea){
        this.regionArea = regionArea;
        if (regionArea != null) {
            this.i18nName = DmI18nUtils.getMessage(regionArea.name());
        }
    }
}
