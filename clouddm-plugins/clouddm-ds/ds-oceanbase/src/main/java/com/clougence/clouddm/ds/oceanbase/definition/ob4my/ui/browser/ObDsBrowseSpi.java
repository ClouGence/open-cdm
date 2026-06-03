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
package com.clougence.clouddm.ds.oceanbase.definition.ob4my.ui.browser;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.ds.oceanbase.dialect.ob4my.ObForMySQLDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.browser.MyDsBrowseSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;

public class ObDsBrowseSpi extends MyDsBrowseSpi {

    protected Dialect dialect() {
        return ObForMySQLDialect.INSTANCE;
    }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_GENERATE, MENU_BROWSE_PROPERTY));
            }
            case RDBProcedure: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_PROCEDURE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROCEDURE_ALTER, MENU_BROWSE_PROCEDURE_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RDBFunction: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_FUNCTION;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_FUNCTION_ALTER, MENU_BROWSE_FUNCTION_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RDBTrigger: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TRIGGER;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TRIGGER_ALTER, MENU_BROWSE_TRIGGER_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
