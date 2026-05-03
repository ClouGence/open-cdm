package com.clougence.clouddm.ds.hana.definition.ui.broswer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

/**
 * @author chunlin
 * @date 2024/4/2
 */
public class HanaDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Arrays.asList(UmiTypes.Catalog, UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Procedure, UmiTypes.Function, UmiTypes.Trigger, UmiTypes.Sequence, UmiTypes.Synonym);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    protected Dialect dialect() {
        return HanaDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.UpperCase; }

    /**
     * Obtain right-click menu based on node type
     * 
     * @param targetType node type
     * @return menu list
     */
    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INSTANCE_TWO_LAYERS;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_CATALOG_CREATE, MENU_BROWSE_SCHEMA_CREATE));
            }
            case RdbCatalog: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_CATALOG;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_CONSOLE, MENU_BROWSE_CATALOG_CREATE));
            }
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE));
            }
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_REQUEST, MENU_BROWSE_PROPERTY));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_REQUEST, MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
