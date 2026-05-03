package com.clougence.clouddm.ds.redis.definition.ui.browser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.redis.dialect.RedisDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class RedisDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Collections.singletonList(UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Key);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    protected Dialect dialect() {
        return RedisDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.Sensitive; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INSTANCE_TWO_LAYERS;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE, MENU_BROWSE_INSTANCE_DROP));
            }
            case RdbCatalog:
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE, MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_SCHEMA_RENAME, MENU_BROWSE_SCHEMA_DROP));
            }
            case Key: {
                List<String> menus = RdbUiMenuDef.DEFAULT_CACHE_KEY;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_KEY_CREATE, MENU_BROWSE_KEY_ALTER, MENU_BROWSE_KEY_DATA, MENU_BROWSE_PROPERTY, MENU_BROWSE_KEY_TRUNCATE));
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
