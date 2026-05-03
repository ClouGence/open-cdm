package com.clougence.clouddm.ds.oceanbase.definition.obforora.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.oceanbase.dialect.obforora.ObForOracleDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class ObForOraDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    protected Dialect dialect() {
        return ObForOracleDialect.INSTANCE;
    }

    @Override
    public List<UmiTypes> getLevels() { return Arrays.asList(UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Procedure, UmiTypes.Function, UmiTypes.Trigger, UmiTypes.Sequence, UmiTypes.Synonym);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.LowerCase; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INSTANCE_TWO_LAYERS;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE));
            }
            case RdbCatalog:
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE, MENU_BROWSE_SCHEMA_RENAME, MENU_BROWSE_SCHEMA_DROP));
            }
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_VIEW_RENAME, MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_VIEW_ALTER, MENU_BROWSE_VIEW_CREATE, MENU_BROWSE_VIEW_COMPILE));
            }
            case RDBFunction: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_FUNCTION;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_FUNCTION_ALTER, MENU_BROWSE_FUNCTION_CREATE, MENU_BROWSE_FUNCTION_COMPILE, MENU_BROWSE_FUNCTION_REQUEST));
            }
            case RDBProcedure: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_PROCEDURE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROCEDURE_ALTER, MENU_BROWSE_PROCEDURE_CREATE, MENU_BROWSE_PROCEDURE_COMPILE, MENU_BROWSE_PROCEDURE_REQUEST));
            }
            case RDBTrigger: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TRIGGER;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TRIGGER_ALTER, MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TRIGGER_COMPILE, MENU_BROWSE_TRIGGER_REQUEST, MENU_BROWSE_PROPERTY));
            }
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_TABLE_ALTER, MENU_BROWSE_TABLE_FAKER, MENU_BROWSE_TABLE_FAKER_INCREMENT, MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TABLE_DATA));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
