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
package com.clougence.clouddm.console.web.global.i18n;

import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.i18n.I18nResource;

/**
 * @author mode create time is 2021/1/30
 **/
@I18nResource("/i18n/ui-menus")
public interface UiMenus18nKey {

    static String findMenuI18n(String menuID) {
        return "UI_MENU_TITLE_" + menuID.toUpperCase();
    }

    String UI_LEAF_TITLE_PREFIX           = "UI_LEAF_TITLE_";
    String UI_LEAF_TITLE_INSTANCE         = UI_LEAF_TITLE_PREFIX + UmiTypes.Instance.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_CATALOG      = UI_LEAF_TITLE_PREFIX + UmiTypes.Catalog.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_SCHEMA       = UI_LEAF_TITLE_PREFIX + UmiTypes.Schema.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_TABLE        = UI_LEAF_TITLE_PREFIX + UmiTypes.Table.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_VIEW         = UI_LEAF_TITLE_PREFIX + UmiTypes.View.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_PROCEDURE    = UI_LEAF_TITLE_PREFIX + UmiTypes.Procedure.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_FUNCTION     = UI_LEAF_TITLE_PREFIX + UmiTypes.Function.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_TRIGGER      = UI_LEAF_TITLE_PREFIX + UmiTypes.Trigger.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_MATERIALIZED = UI_LEAF_TITLE_PREFIX + UmiTypes.Materialized.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_SEQUENCE     = UI_LEAF_TITLE_PREFIX + UmiTypes.Sequence.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_SYNONYM      = UI_LEAF_TITLE_PREFIX + UmiTypes.Synonym.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_COLUMN       = UI_LEAF_TITLE_PREFIX + UmiTypes.Column.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_TOPIC            = UI_LEAF_TITLE_PREFIX + UmiTypes.Topic.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_KEY              = UI_LEAF_TITLE_PREFIX + UmiTypes.Key.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_ROLE             = UI_LEAF_TITLE_PREFIX + UmiTypes.ROLE.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_USER             = UI_LEAF_TITLE_PREFIX + UmiTypes.USER.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_TABLESPACE       = UI_LEAF_TITLE_PREFIX + UmiTypes.TABLESPACE.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_EXTERNAL_TABLE   = UI_LEAF_TITLE_PREFIX + UmiTypes.ExternalTable.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_DBLINK           = UI_LEAF_TITLE_PREFIX + UmiTypes.DBLink.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_JOB              = UI_LEAF_TITLE_PREFIX + UmiTypes.Job.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_SCHEDULE_JOB     = UI_LEAF_TITLE_PREFIX + UmiTypes.ScheduleJob.getTypeName().toUpperCase();

    static String findI18nKey(UmiTypes umiTypes) {
        switch (umiTypes) {
            case Instance:
                return UI_LEAF_TITLE_INSTANCE;
            case Catalog:
                return UI_LEAF_TITLE_RDB_CATALOG;
            case Schema:
                return UI_LEAF_TITLE_RDB_SCHEMA;
            case Table:
                return UI_LEAF_TITLE_RDB_TABLE;
            case View:
                return UI_LEAF_TITLE_RDB_VIEW;
            case Procedure:
                return UI_LEAF_TITLE_RDB_PROCEDURE;
            case Function:
                return UI_LEAF_TITLE_RDB_FUNCTION;
            case Trigger:
                return UI_LEAF_TITLE_RDB_TRIGGER;
            case Materialized:
                return UI_LEAF_TITLE_RDB_MATERIALIZED;
            case Sequence:
                return UI_LEAF_TITLE_RDB_SEQUENCE;
            case Synonym:
                return UI_LEAF_TITLE_RDB_SYNONYM;
            case Column:
                return UI_LEAF_TITLE_RDB_COLUMN;
            case Topic:
                return UI_LEAF_TITLE_TOPIC;
            case Key:
                return UI_LEAF_TITLE_KEY;
            case ROLE:
                return UI_LEAF_TITLE_ROLE;
            case USER:
                return UI_LEAF_TITLE_USER;
            case TABLESPACE:
                return UI_LEAF_TITLE_TABLESPACE;
            case ExternalTable:
                return UI_LEAF_TITLE_EXTERNAL_TABLE;
            case DBLink:
                return UI_LEAF_TITLE_DBLINK;
            case Job:
                return UI_LEAF_TITLE_JOB;
            case ScheduleJob:
                return UI_LEAF_TITLE_SCHEDULE_JOB;
            case Endpoint:
            default:
                return "";
        }
    }

    String UI_LEAF_TITLE_RDB_PREFIX            = "UI_LEAF_TITLE_RDB_";
    String UI_LEAF_TITLE_RDB_COLUMN_GROUP      = UI_LEAF_TITLE_RDB_PREFIX + DsMenuType.RdbColumnGroup.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_PRIMARY_GROUP     = UI_LEAF_TITLE_RDB_PREFIX + DsMenuType.RdbPrimaryGroup.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_INDEX_GROUP       = UI_LEAF_TITLE_RDB_PREFIX + DsMenuType.RdbIndexGroup.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_PARTITION_GROUP   = UI_LEAF_TITLE_RDB_PREFIX + DsMenuType.RdbPartitionGroup.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_PARAM_GROUP       = UI_LEAF_TITLE_RDB_PREFIX + DsMenuType.RdbParamGroup.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_RETURNS           = UI_LEAF_TITLE_RDB_PREFIX + DsMenuType.RdbReturns.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_CONSTRAINT_GROUP  = UI_LEAF_TITLE_RDB_PREFIX + DsMenuType.RdbConstraintGroup.getTypeName().toUpperCase();
    String UI_LEAF_TITLE_RDB_FOREIGN_KEY_GROUP = UI_LEAF_TITLE_RDB_PREFIX + DsMenuType.RdbForeignGroup.getTypeName().toUpperCase();

}
