package com.clougence.clouddm.ds.tidb.definition.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.tidb.dialect.TiDBDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.browser.MyDsBrowseSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class TiDsBrowseSpi extends MyDsBrowseSpi {

    protected Dialect dialect() {
        return TiDBDialect.INSTANCE;
    }

    /**
     * According to the
     * <a href="https://docs.pingcap.com/zh/tidb/stable/mysql-compatibility">docs v7.5</a>,
     *  TiDB presently lacks support for a part of MySQL features like PROCEDURE, FUNCTION, TRIGGER.
     */
    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Sequence);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_GENERATE, MENU_BROWSE_TRIGGER_CREATE));
            }
            default:
                return super.getMenus(targetType);
        }
    }

}
