package com.clougence.clouddm.ds.dameng.definition.ui.browser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.dameng.dialect.DmDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class DmDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Arrays.asList(UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> defaultList = Arrays
            .asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized, UmiTypes.Procedure, UmiTypes.Function, UmiTypes.Trigger, UmiTypes.Sequence, UmiTypes.Synonym, UmiTypes.ROLE, UmiTypes.USER, UmiTypes.TABLESPACE);
        return CollectionUtils.asMap(UmiTypes.Schema, defaultList);
    }

    @Override
    protected Dialect dialect() {
        return DmDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.Sensitive; }

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
            case RdbView: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_VIEW;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_VIEW_RENAME, MENU_BROWSE_VIEW_REQUEST, MENU_BROWSE_VIEW_COMPILE, MENU_BROWSE_PROPERTY));
            }
            case RdbTable: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TABLE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_PROPERTY));
            }
            case RDBTrigger: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_TRIGGER;
                return filterMenus(menus, Arrays
                    .asList(MENU_BROWSE_TRIGGER_CREATE, MENU_BROWSE_TRIGGER_ALTER, MENU_BROWSE_TRIGGER_COMPILE, MENU_BROWSE_TRIGGER_REQUEST, MENU_BROWSE_PROPERTY));
            }
            case RDBFunction: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_FUNCTION;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_FUNCTION_ALTER, MENU_BROWSE_FUNCTION_COMPILE, MENU_BROWSE_FUNCTION_REQUEST, MENU_BROWSE_PROPERTY));
            }
            case RDBProcedure: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_PROCEDURE;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROCEDURE_ALTER, MENU_BROWSE_PROCEDURE_COMPILE, MENU_BROWSE_PROCEDURE_REQUEST, MENU_BROWSE_PROPERTY));
            }
            case RdbConstraint: {
                return RdbUiMenuDef.DEFAULT_RDB_CONSTRAINT;
            }
            case RdbUser: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_USER;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_PROPERTY));
            }
            default:
                return super.getMenus(targetType);
        }
    }

}
