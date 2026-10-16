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
package com.clougence.clouddm.ds.es.definition.ui;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.i18n.I18nUtils;

public class EsDsBrowseSpi implements DsBrowseSpi, DsFeatureIDs {
    @Override
    public List<UmiTypes> getLevels() { return List.of(); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() { return Map.of(UmiTypes.Instance, List.of(UmiTypes.Table)); }

    @Override
    public Map<UmiTypes, String> getLeafLabels() { return Map.of(UmiTypes.Table, I18nUtils.initI18n(EsDsI18nKeys.class).getMessage(EsDsI18nKeys.ES_INDICES)); }

    @Override
    public List<UmiTypes> getLeafExpand() { return List.of(UmiTypes.Table); }

    @Override
    public String getLeftQualifier() { return ""; }

    @Override
    public String getRightQualifier() { return ""; }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.Sensitive; }

    @Override
    public List<String> getMenus(DsMenuType type) {
        return switch (type) {
            case Instance -> List.of(MENU_BROWSE_CONSOLE, MENU_BROWSE_REFRESH, MENU_BROWSE_COPY_NAME);
            case RdbTable -> List.of(MENU_BROWSE_COMMAND_TEMPLATE, MENU_BROWSE_REFRESH, MENU_BROWSE_COPY_NAME);
            case RdbColumn -> List.of(MENU_BROWSE_COPY_NAME);
            default -> List.of();
        };
    }
}
