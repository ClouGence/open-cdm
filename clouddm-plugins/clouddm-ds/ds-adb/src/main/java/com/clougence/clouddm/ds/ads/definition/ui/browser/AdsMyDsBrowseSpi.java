package com.clougence.clouddm.ds.ads.definition.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.ads.dialect.ads4my.AdbMySqlDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.browser.MyDsBrowseSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class AdsMyDsBrowseSpi extends MyDsBrowseSpi {

    @Override
    protected Dialect dialect() {
        return AdbMySqlDialect.INSTANCE;
    }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> defaultList = Arrays.asList(UmiTypes.Table, UmiTypes.View);
        return CollectionUtils.asMap(UmiTypes.Schema, defaultList);
    }

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
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_PROPERTY));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_RENAME, MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
