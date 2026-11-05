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
package com.clougence.clouddm.ds.iotdb.metadata;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.i18n.I18nUtils;

public class IotdbBrowseSpi implements DsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return List.of(UmiTypes.Catalog, UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() { return Map.of(UmiTypes.Schema, List.of(UmiTypes.Device, UmiTypes.Timeseries, UmiTypes.Table, UmiTypes.View)); }

    @Override
    public Map<UmiTypes, String> getLeafLabels() {
        I18nUtils i18n = I18nUtils.initI18n(IotdbI18nKeys.class);
        return Map.of(UmiTypes.Device, i18n.getMessage(IotdbI18nKeys.IOTDB_DEVICES), UmiTypes.Timeseries, i18n.getMessage(IotdbI18nKeys.IOTDB_TIMESERIES));
    }

    @Override
    public List<UmiTypes> getLeafExpand() { return List.of(UmiTypes.Table, UmiTypes.View); }

    @Override
    public String getLeftQualifier() { return ""; }

    @Override
    public String getRightQualifier() { return ""; }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.Sensitive; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        return List.of();
    }
}
