package com.clougence.clouddm.dsfamily.postgres.definition.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class PgDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Arrays.asList(UmiTypes.Catalog, UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized, UmiTypes.Procedure, UmiTypes.Function, UmiTypes.Trigger, UmiTypes.Sequence);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    protected Dialect dialect() {
        return PostgreDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.LowerCase; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case RdbCatalog: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_CATALOG;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_CONSOLE));
            }
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_REQUEST, MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_PROPERTY));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_ALTER, MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RdbMaterialized: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_MATERIALIZED;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_MATERIALIZED_DROP, MENU_BROWSE_PROPERTY, MENU_BROWSE_MATERIALIZED_REQUEST));
            }
            case RDBTrigger: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TRIGGER;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TRIGGER_ALTER, MENU_BROWSE_TRIGGER_COMPILE, MENU_BROWSE_PROPERTY, MENU_BROWSE_TRIGGER_REQUEST));
            }
            case RDBFunction: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_FUNCTION;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_FUNCTION_CREATE, MENU_BROWSE_FUNCTION_ALTER, MENU_BROWSE_FUNCTION_DROP, MENU_BROWSE_FUNCTION_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RDBProcedure: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_PROCEDURE;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_PROCEDURE_CREATE, MENU_BROWSE_PROCEDURE_ALTER, MENU_BROWSE_PROCEDURE_DROP, MENU_BROWSE_PROCEDURE_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RdbSequence: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SEQUENCE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROPERTY));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
