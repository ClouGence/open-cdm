package com.clougence.clouddm.ds.gauss.broswer.gsog;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.browser.PgDsBrowseSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class GsogDsBrowseSpi extends PgDsBrowseSpi {

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized, UmiTypes.Function, UmiTypes.Procedure, UmiTypes.Sequence);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    public List<UmiTypes> getLeafExpand() { return Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized, UmiTypes.Function, UmiTypes.Procedure); }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_REQUEST, MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_PROPERTY, MENU_BROWSE_TABLE_GET_DDL));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_ALTER, MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RdbMaterialized: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_MATERIALIZED;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROPERTY, MENU_BROWSE_MATERIALIZED_DROP, MENU_BROWSE_MATERIALIZED_REQUEST));
            }
            case RDBFunction: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_FUNCTION;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_FUNCTION_CREATE, MENU_BROWSE_FUNCTION_ALTER, MENU_BROWSE_FUNCTION_DROP, MENU_BROWSE_FUNCTION_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RDBProcedure: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_PROCEDURE;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_PROCEDURE_ALTER, MENU_BROWSE_PROCEDURE_DROP, MENU_BROWSE_PROCEDURE_COMPILE, MENU_BROWSE_PROPERTY, MENU_BROWSE_PROCEDURE_CREATE));
            }

            default:
                return super.getMenus(targetType);
        }
    }
}
