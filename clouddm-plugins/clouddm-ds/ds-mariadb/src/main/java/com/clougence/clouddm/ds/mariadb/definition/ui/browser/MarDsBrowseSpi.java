package com.clougence.clouddm.ds.mariadb.definition.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.browser.MyDsBrowseSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class MarDsBrowseSpi extends MyDsBrowseSpi {

    /**
     * According to the
     * <a href="https://docs.pingcap.com/zh/tidb/stable/mysql-compatibility">docs v7.5</a>,
     *  TiDB presently lacks support for a part of MySQL features like PROCEDURE, FUNCTION, TRIGGER.
     */
    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY));
            }
            default: {
                return super.getMenus(targetType);
            }
        }
    }
}
