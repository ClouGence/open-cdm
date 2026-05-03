package com.clougence.clouddm.ds.clickhouse.i18n;

import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.utils.i18n.I18nResource;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
@I18nResource("/META-INF/clougence/i18n/ch-ui-editor-table")
public interface ChDsI18nKeys extends DsDataEditorI18nKeys, DsTableEditorI18nKeys {

    String REWRITE_LIMIT_LABEL = "REWRITE_LIMIT_LABEL";
}
