package com.clougence.clouddm.ds.maxcompute.definition.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.dialect.McDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class McDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Arrays.asList(UmiTypes.Catalog, UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays
            .asList(UmiTypes.Table, /*UmiTypes.ExternalTable,*/ UmiTypes.View, UmiTypes.Materialized, UmiTypes.Function, UmiTypes.USER, UmiTypes.ROLE);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    public List<UmiTypes> getLeafExpand() { return Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized/*, UmiTypes.ExternalTable*/); }

    @Override
    protected Dialect dialect() {
        return McDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.Sensitive; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INSTANCE_THREE_LAYERS;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_CATALOG_CREATE, MENU_BROWSE_INSTANCE_CREATE, MENU_BROWSE_INSTANCE_RENAME, MENU_BROWSE_INSTANCE_DROP));
            }
            case RdbCatalog: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_CATALOG;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_CATALOG_CREATE, MENU_BROWSE_CATALOG_RENAME, MENU_BROWSE_CATALOG_DROP, MENU_BROWSE_CONSOLE));
            }
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_RENAME));
            }
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(//
                        MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_TABLE_RENAME, MENU_BROWSE_TABLE_ALTER, MENU_BROWSE_TRIGGER_CREATE));
            }
            case RdbExternalTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(//
                        MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_TABLE_RENAME, MENU_BROWSE_TABLE_ALTER, MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TABLE_DROP, MENU_BROWSE_TABLE_TRUNCATE, MENU_BROWSE_TABLE_GET_DDL));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_CREATE, MENU_BROWSE_VIEW_RENAME, MENU_BROWSE_VIEW_ALTER, MENU_BROWSE_VIEW_COMPILE));
            }
            case RdbMaterialized: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_MATERIALIZED;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_MATERIALIZED_REQUEST));
            }
            case RdbUser: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_USER;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROPERTY, MENU_BROWSE_USER_DROP));
            }
            default:
                return super.getMenus(targetType);
        }
    }

}
