package com.clougence.clouddm.ds.mongodb.definition.ui.browser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.clouddm.dsfamily.schema.dialect.DefaultDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class MongoDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Collections.singletonList(UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    protected Dialect dialect() {
        return DefaultDialect.DEFAULT;
    }

    @Override
    public List<UmiTypes> getLeafExpand() {

        return Arrays.asList(UmiTypes.Table);
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.Sensitive; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INSTANCE_TWO_LAYERS;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_INSTANCE_DROP));
            }
            case RdbCatalog:
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_SCHEMA_RENAME, MENU_BROWSE_SCHEMA_DROP));
            }
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TABLE_ALTER,//
                        MENU_BROWSE_TABLE_DATA, MENU_BROWSE_PROPERTY, MENU_BROWSE_TABLE_TRUNCATE, MENU_BROWSE_TABLE_FAKER, MENU_BROWSE_TABLE_FAKER_INCREMENT,//
                        MENU_BROWSE_TABLE_GENERATE, MENU_BROWSE_TABLE_GET_DDL, MENU_BROWSE_PERMISSIONS));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_CREATE, MENU_BROWSE_VIEW_RENAME, MENU_BROWSE_VIEW_ALTER, MENU_BROWSE_PROPERTY, MENU_BROWSE_VIEW_COMPILE//
                        , MENU_BROWSE_VIEW_REQUEST, MENU_BROWSE_PERMISSIONS));
            }
            case RdbIndex: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INDEX;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TABLE_DATA, MENU_BROWSE_TABLE_TRUNCATE, MENU_BROWSE_INDEX_DROP));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
