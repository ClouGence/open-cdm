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
package com.clougence.clouddm.ds.oracle.definition.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class OraDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Arrays.asList(UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays
            .asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized, UmiTypes.Procedure, UmiTypes.Function, UmiTypes.Trigger, UmiTypes.Sequence, UmiTypes.Synonym, UmiTypes.USER, UmiTypes.ROLE, UmiTypes.DBLink, UmiTypes.Job, UmiTypes.ScheduleJob);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    public List<UmiTypes> getLeafExpand() { return Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Function, UmiTypes.Procedure); }

    @Override
    protected Dialect dialect() {
        return OracleDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.LowerCase; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INSTANCE_TWO_LAYERS;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE));
            }
            case RdbCatalog:
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE, MENU_BROWSE_SCHEMA_RENAME, MENU_BROWSE_SCHEMA_DROP));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_RENAME, MENU_BROWSE_TABLE_CREATE));
            }
            case RDBFunction: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_FUNCTION;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_FUNCTION_ALTER));
            }
            case RDBProcedure: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_PROCEDURE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROCEDURE_ALTER));
            }
            case RdbSynonym: {
                return RdbUiMenuDef.DEFAULT_RDB_SYNONYM;
            }
            case RdbMaterialized: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_MATERIALIZED;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_MATERIALIZED_DROP, MENU_BROWSE_MATERIALIZED_REQUEST, MENU_BROWSE_PROPERTY));
            }
            case RdbDBLink: {
                return RdbUiMenuDef.DEFAULT_RDB_DBLINK;
            }
            case RdbJob: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_JOB;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_JOB_CREATE, MENU_BROWSE_PROPERTY));
            }
            case RdbScheduleJob: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEDULE_JOB;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_JOB_CREATE, MENU_BROWSE_PROPERTY));
            }
            case RdbSequence: {
                return RdbUiMenuDef.DEFAULT_RDB_SEQUENCE;
            }
            case RdbUser: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_USER;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_USER_DROP));
            }
            case RdbRole: {
                return RdbUiMenuDef.DEFAULT_RDB_ROLE;
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
