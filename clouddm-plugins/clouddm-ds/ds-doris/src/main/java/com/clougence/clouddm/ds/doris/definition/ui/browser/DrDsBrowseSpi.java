package com.clougence.clouddm.ds.doris.definition.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.doris.dialect.DorisDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class DrDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Arrays.asList(UmiTypes.Catalog, UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList1 = Arrays.asList(UmiTypes.Table, UmiTypes.View, /*UmiTypes.Function,*/ UmiTypes.Materialized);
        List<UmiTypes> schemaList2 = Arrays.asList(UmiTypes.ExternalTable);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList1, UmiTypes.ExternalSchema, schemaList2);
    }

    @Override
    protected Dialect dialect() {
        return DorisDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.LowerCase; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INSTANCE_THREE_LAYERS;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_CATALOG_CREATE, MENU_BROWSE_INSTANCE_RENAME, MENU_BROWSE_INSTANCE_DROP));
            }
            case RdbCatalog: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_CATALOG;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_CATALOG_CREATE, MENU_BROWSE_CATALOG_RENAME, MENU_BROWSE_CATALOG_DROP, MENU_BROWSE_CONSOLE));
            }
            case RdbExternalCatalog: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_EXTERNAL_CATALOG;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_CATALOG_CREATE, MENU_BROWSE_CATALOG_RENAME, MENU_BROWSE_CATALOG_DROP, MENU_BROWSE_CONSOLE));
            }
            case RdbSchema: {
                return RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
            }
            case RdbExternalSchema:
                return RdbUiMenuDef.DEFAULT_RDB_EXTERNAL_SCHEMA;
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_PROPERTY));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_RENAME, MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RdbMaterialized: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_MATERIALIZED;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_MATERIALIZED_DROP, MENU_BROWSE_MATERIALIZED_REQUEST, MENU_BROWSE_PROPERTY));
            }
            case RdbExternalTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_EXTERNAL_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROPERTY, MENU_BROWSE_TABLE_DROP));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
