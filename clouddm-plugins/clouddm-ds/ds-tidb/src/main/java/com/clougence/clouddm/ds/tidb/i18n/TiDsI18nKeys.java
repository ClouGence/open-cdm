package com.clougence.clouddm.ds.tidb.i18n;

import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.utils.i18n.I18nResource;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
@I18nResource("/META-INF/clougence/i18n/ti-ui-editor-table")
public interface TiDsI18nKeys extends MyDsI18nKeys {

    String EDITOR_TABLEINFO_SHARD_ROW_ID_BITS_TITLE = "UI_EDITOR_TABLEINFO_SHARD_ROW_ID_BITS_TITLE";
    String EDITOR_TABLEINFO_SHARD_ROW_ID_BITS_DESC  = "UI_EDITOR_TABLEINFO_SHARD_ROW_ID_BITS_DESC";
    String EDITOR_TABLEINFO_PRE_SPLIT_REGIONS_TITLE = "UI_EDITOR_TABLEINFO_PRE_SPLIT_REGIONS_TITLE";
    String EDITOR_TABLEINFO_PRE_SPLIT_REGIONS_DESC  = "UI_EDITOR_TABLEINFO_PRE_SPLIT_REGIONS_DESC";
}
