package com.clougence.clouddm.dsfamily.mysql.definition.ui.browser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class MyDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Collections.singletonList(UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> defaultList = Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Procedure, UmiTypes.Function, UmiTypes.Trigger);
        return CollectionUtils.asMap(UmiTypes.Schema, defaultList);
    }

    @Override
    protected Dialect dialect() {
        return MySqlDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.LowerCase; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance:
                return RdbUiMenuDef.DEFAULT_RDB_INSTANCE_TWO_LAYERS;
            case RdbCatalog:
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_RENAME));
            }
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_GENERATE));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_COMPILE));
            }
            case RdbPrimary: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_KEY;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PRIMARY_RENAME));
            }
            case RDBProcedure: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_PROCEDURE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROCEDURE_ALTER, MENU_BROWSE_PROCEDURE_COMPILE));
            }
            case RDBFunction: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_FUNCTION;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_FUNCTION_ALTER, MENU_BROWSE_FUNCTION_COMPILE));
            }
            case RDBTrigger: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TRIGGER;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TRIGGER_ALTER, MENU_BROWSE_TRIGGER_COMPILE));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
