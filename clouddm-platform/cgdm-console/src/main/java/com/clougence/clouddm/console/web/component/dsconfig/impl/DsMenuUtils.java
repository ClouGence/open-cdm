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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.util.*;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsMenu;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.UiMenus18nKey;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020/11/7 14:27
 */
@Slf4j
public class DsMenuUtils implements DsFeatureIDs {

    // targetName not null.
    public final static List<String>         NEED_TARGET_MENU_IDS = Arrays.asList(//
            // -- commons
            MENU_BROWSE_COPY_NAME,                  // UIPanel: copy
            MENU_BROWSE_PROPERTY,//
            MENU_BROWSE_REFRESH,                    // Browse: refresh table
            // -- tables
            MENU_BROWSE_TABLE_ALTER,                // UIPanel: alter table
            MENU_BROWSE_TABLE_DROP,                 // Action: drop table
            MENU_BROWSE_TABLE_RENAME,               // Action: rename table
            MENU_BROWSE_TABLE_TRUNCATE,             // Action: truncate table
            MENU_BROWSE_TABLE_DATA,                 // UIPanel: data editor
            MENU_BROWSE_TABLE_FAKER,                // UIPanel: gen Data use Faker
            MENU_BROWSE_TABLE_FAKER_INCREMENT,      // UIPanel:
            MENU_BROWSE_TABLE_IMPORT,               // UIPanel: data import
            MENU_BROWSE_TABLE_EXPORT,               // UIPanel: data export
            MENU_BROWSE_TABLE_REQUEST,              // Action: request script
            MENU_BROWSE_TABLE_GENERATE,             // Action: generate script
            MENU_BROWSE_TABLE_GET_DDL,              // Action: get_ddl

            // -- views
            MENU_BROWSE_VIEW_ALTER,         // UIPanel: alter view
            MENU_BROWSE_VIEW_DROP,          // Action: drop view
            MENU_BROWSE_VIEW_RENAME,        // Action: rename view
            MENU_BROWSE_VIEW_DATA,          // UIPanel: data viewed
            MENU_BROWSE_VIEW_EXPORT,        // Action: request script
            MENU_BROWSE_VIEW_REQUEST,       // Action: generate script
            MENU_BROWSE_VIEW_COMPILE,       //
            // -- MENU_BROWSE_MATERIALIZED_REQUEST
            MENU_BROWSE_MATERIALIZED_REQUEST,//
            // -- dblink
            MENU_BROWSE_DBLINK_DROP,//
            MENU_BROWSE_DBLINK_TEST,//
            // -- job
            MENU_BROWSE_JOB_DROP,//
            MENU_BROWSE_JOB_ALTER,//
            MENU_BROWSE_JOB_RUN,//
            MENU_BROWSE_JOB_DISABLE,//
            MENU_BROWSE_JOB_ENABLE,//
            // -- schedule job
            MENU_BROWSE_SCHEDULE_DROP,//
            MENU_BROWSE_SCHEDULE_DISABLE,//
            MENU_BROWSE_SCHEDULE_ENABLE,//
            MENU_BROWSE_SCHEDULE_RUN,//
            // -- columns
            MENU_BROWSE_COLUMN_RENAME,      // Action: rename column
            MENU_BROWSE_COLUMN_ALTER,       // UIPanel: alter column
            MENU_BROWSE_COLUMN_TRUNCATE,    // Action: update to null or reAdd
            MENU_BROWSE_COLUMN_DROP,        // Action: drop column
            MENU_BROWSE_COLUMN_GENERATE,    // Action: generate script
            // -- primary key
            MENU_BROWSE_PRIMARY_RENAME,     // Action: rename primary key
            MENU_BROWSE_PRIMARY_ALTER,      // UIPanel: alter primary key
            MENU_BROWSE_PRIMARY_DROP,       // Action: drop primary key
            MENU_BROWSE_PRIMARY_GENERATE,   // Action: generate script
            // -- index
            MENU_BROWSE_INDEX_RENAME,       // Action: rename index
            MENU_BROWSE_INDEX_ALTER,        // UIPanel: alter index
            MENU_BROWSE_INDEX_DROP,         // Action: drop index
            MENU_BROWSE_INDEX_GENERATE,     // Action: generate script
            // -- partition
            MENU_BROWSE_PARTITION_ALTER,    // UIPanel: alter partition
            MENU_BROWSE_PARTITION_DROP,     // Action: drop partition
            MENU_BROWSE_PARTITION_GENERATE, // Action: generate script
            // -- key
            MENU_BROWSE_KEY_RENAME,         // Action: rename key
            MENU_BROWSE_KEY_ALTER,          // UIPanel: alter key (e.g. Redis)
            MENU_BROWSE_KEY_DROP,        // Action: drop key
            // -- trigger
            MENU_BROWSE_TRIGGER_DROP,  // Action: drop trigger
            MENU_BROWSE_TRIGGER_CREATE, // Object: create trigger
            MENU_BROWSE_TRIGGER_ALTER,   // Object: alter trigger
            MENU_BROWSE_TRIGGER_COMPILE,//
            MENU_BROWSE_TRIGGER_REQUEST,//
            // -- procedure
            MENU_BROWSE_PROCEDURE_DROP,  // Action: drop procedure
            MENU_BROWSE_PROCEDURE_ALTER,  // Action: alter procedure
            MENU_BROWSE_PROCEDURE_COMPILE,//
            MENU_BROWSE_PROCEDURE_REQUEST,//
            // -- function
            MENU_BROWSE_FUNCTION_DROP, // Action: drop function
            MENU_BROWSE_FUNCTION_ALTER,// Action: alter function
            MENU_BROWSE_FUNCTION_COMPILE,//
            MENU_BROWSE_FUNCTION_REQUEST,//
            // -- constraint
            MENU_BROWSE_CONSTRAINT_ENABLE,// Action: enable constraint
            MENU_BROWSE_CONSTRAINT_DISABLE,// Action: disable constraint
            // --SYNONYM
            MENU_BROWSE_SYNONYM_REQUEST,//
            // -- user
            MENU_BROWSE_USER_DROP);

    private final static Map<String, DsMenu> DS_MENU_CACHE        = new HashMap<>();

    private static DsMenu loadMenu(String menuID) {
        if (DS_MENU_CACHE.containsKey(menuID)) {
            return DS_MENU_CACHE.get(menuID);
        } else {
            DsMenu dsMenu = new DsMenu();
            dsMenu.setMenuId(menuID);

            dsMenu.setI18n(DmI18nUtils.getMessage(UiMenus18nKey.findMenuI18n(menuID)));
            dsMenu.setNeedTarget(NEED_TARGET_MENU_IDS.contains(menuID));

            DS_MENU_CACHE.put(menuID, dsMenu);
            return dsMenu;
        }
    }

    public static boolean isSeparator(DsMenu dsMenu) {
        return dsMenu.getMenuId().equals(MENU_SEPARATOR);
    }

    public static List<DsMenu> generationDsMenus(List<String> menuTemp) {
        List<DsMenu> dsMenus = new ArrayList<>();
        for (String curMenuId : menuTemp) {
            DsMenu dsMenu = loadMenu(curMenuId);
            if (isSeparator(dsMenu)) {
                if (dsMenus.isEmpty()) {
                    continue;// first menu can not be separator
                }
                if (isSeparator(dsMenus.get(dsMenus.size() - 1))) {
                    continue;// can not be continuous separator
                }
            }

            dsMenus.add(dsMenu);
        }

        return dsMenus;
    }
}
