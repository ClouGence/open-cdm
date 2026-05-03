package com.clougence.clouddm.ds.hologres.broswer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.browser.PgDsBrowseSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class HgDsBrowseSpi extends PgDsBrowseSpi {

    //
    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_TABLE_REQUEST, MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_PROPERTY, MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_TABLE_ALTER, MENU_BROWSE_TABLE_GET_DDL));
            }
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_CREATE));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_ALTER, MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY, MENU_BROWSE_VIEW_CREATE));
            }
            default: {
                return super.getMenus(targetType);
            }
        }
    }

}
