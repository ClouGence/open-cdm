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
package com.clougence.clouddm.console.web.component.dsconfig.mode;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DsClassify;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsConfig {

    private DsClassify                classify;
    private Map<String, Object>       features;
    private DsConstantConfig          constant;
    private DsCategories              categories;
    private Map<String, List<DsMenu>> menus;
    private List<String>              targetDsList;
    private List<String>              ddlList;
    private List<DsIsolation>         isolations;
    private List<DsDriverFamily>      driverFamilies;
    private DsLanguage                language;

    @Override
    public DsConfig clone() {
        DsConfig dsConfig = new DsConfig();
        dsConfig.setClassify(this.classify);
        dsConfig.setFeatures(this.features.isEmpty() ? Collections.emptyMap() : new HashMap<>(this.features));
        dsConfig.setConstant(this.constant.clone());
        dsConfig.setCategories(this.categories.clone());
        if (this.menus.isEmpty()) {
            dsConfig.setMenus(Collections.emptyMap());
        } else {
            Map<String, List<DsMenu>> menuMap = new HashMap<>();
            this.menus.forEach((k, v) -> {
                menuMap.put(k, v.isEmpty() ? Collections.emptyList() : v.stream().map(DsMenu::clone).collect(Collectors.toList()));
            });
            dsConfig.setMenus(menuMap);
        }
        dsConfig.setTargetDsList(this.targetDsList.isEmpty() ? Collections.emptyList() : new ArrayList<>(this.targetDsList));
        dsConfig.setDdlList(this.ddlList.isEmpty() ? Collections.emptyList() : new ArrayList<>(this.ddlList));
        dsConfig.setIsolations(this.isolations.isEmpty() ? Collections.emptyList() : this.isolations.stream().map(DsIsolation::clone).collect(Collectors.toList()));
        dsConfig.setDriverFamilies(this.driverFamilies.isEmpty() ? Collections.emptyList() : this.driverFamilies.stream().map(DsDriverFamily::clone).collect(Collectors.toList()));
        dsConfig.setLanguage(this.language == null ? null : this.language.clone());
        return dsConfig;
    }
}
