package com.clougence.clouddm.dsfamily.definition.ui.browser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;

public abstract class AbstractDsBrowseSpi implements DsBrowseSpi, DsFeatureIDs {

    protected abstract Dialect dialect();

    @Override
    public String getLeftQualifier() { return dialect().leftQualifier(); }

    @Override
    public String getRightQualifier() { return dialect().rightQualifier(); }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Env:
                return RdbUiMenuDef.DEFAULT_ENV;
            case Instance:
                return RdbUiMenuDef.DEFAULT_RDB_INSTANCE_THREE_LAYERS;
            case RdbCatalog:
                return RdbUiMenuDef.DEFAULT_RDB_CATALOG;
            case RdbSchema:
                return RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
            case RdbTable:
                return RdbUiMenuDef.DEFAULT_RDB_TABLE;
            case RdbView:
                return RdbUiMenuDef.DEFAULT_RDB_VIEW;
            case RdbColumn:
                return RdbUiMenuDef.DEFAULT_RDB_COLUMN;
            case RdbColumnGroup: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_COLUMN;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_COLUMN_RENAME, MENU_BROWSE_COLUMN_ALTER, MENU_BROWSE_COLUMN_DROP, MENU_BROWSE_COLUMN_GENERATE, MENU_BROWSE_COPY_NAME));
            }
            case RdbPrimary:
                return RdbUiMenuDef.DEFAULT_RDB_KEY;
            case RdbPrimaryGroup: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_KEY;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_PRIMARY_RENAME, MENU_BROWSE_PRIMARY_ALTER, MENU_BROWSE_PRIMARY_DROP, MENU_BROWSE_PRIMARY_GENERATE, MENU_BROWSE_COPY_NAME));
            }
            case RdbIndex:
                return RdbUiMenuDef.DEFAULT_RDB_INDEX;
            case RdbIndexGroup: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INDEX;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_INDEX_RENAME, MENU_BROWSE_INDEX_ALTER, MENU_BROWSE_INDEX_DROP, MENU_BROWSE_INDEX_GENERATE, MENU_BROWSE_COPY_NAME));
            }
            case RdbPartition:
                return RdbUiMenuDef.DEFAULT_RDB_PARTITION;
            case RdbPartitionGroup: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_PARTITION;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PARTITION_ALTER, MENU_BROWSE_PARTITION_DROP, MENU_BROWSE_PARTITION_GENERATE, MENU_BROWSE_COPY_NAME));
            }
            case RDBProcedure: {
                return RdbUiMenuDef.DEFAULT_RDB_PROCEDURE;
            }
            case RDBTrigger: {
                return RdbUiMenuDef.DEFAULT_RDB_TRIGGER;
            }
            case RdbMaterialized: {
                return RdbUiMenuDef.DEFAULT_RDB_MATERIALIZED;
            }
            case RdbUser: {
                return RdbUiMenuDef.DEFAULT_RDB_USER;
            }
            case RdbRole: {
                return RdbUiMenuDef.DEFAULT_RDB_ROLE;
            }
            case Unknown:
            default:
                return RdbUiMenuDef.DEFAULT_EMPTY;
        }
    }

    protected List<String> filterMenus(List<String> data, List<String> filter) {
        List<String> copy = new ArrayList<>(data);
        copy.removeAll(filter);
        return copy;
    }

    @Override
    public List<UmiTypes> getLeafExpand() {
        return Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized,//
                UmiTypes.Function, UmiTypes.Procedure, UmiTypes.Index, UmiTypes.ExternalTable);
    }
}
