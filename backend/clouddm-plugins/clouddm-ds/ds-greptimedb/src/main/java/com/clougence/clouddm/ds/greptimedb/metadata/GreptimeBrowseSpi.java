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

package com.clougence.clouddm.ds.greptimedb.metadata;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.browser.MyDsBrowseSpi;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.struts.UmiTypes;

public class GreptimeBrowseSpi extends MyDsBrowseSpi {

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() { return Map.of(UmiTypes.Schema, List.of(UmiTypes.Table, UmiTypes.View)); }

    @Override
    public List<UmiTypes> getLeafExpand() { return List.of(UmiTypes.Table, UmiTypes.View); }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.Sensitive; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        return switch (targetType) {
            case Instance, RdbSchema -> List.of(MENU_BROWSE_CONSOLE, MENU_BROWSE_REFRESH, MENU_BROWSE_COPY_NAME, MENU_BROWSE_PERMISSIONS);
            case RdbTable -> List.of(MENU_BROWSE_TABLE_REQUEST, MENU_BROWSE_REFRESH, MENU_BROWSE_COPY_NAME, MENU_BROWSE_PERMISSIONS);
            case RdbView -> List.of(MENU_BROWSE_VIEW_REQUEST, MENU_BROWSE_REFRESH, MENU_BROWSE_COPY_NAME);
            case RdbColumn, RdbPrimary, RdbIndex -> List.of(MENU_BROWSE_COPY_NAME);
            case Env -> super.getMenus(targetType);
            default -> List.of();
        };
    }
}
