package com.clougence.clouddm.ds.oceanbase.i18n;

import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.utils.i18n.I18nResource;

/**
 * @Author: Ekko
 * @Date: 2023-09-14 15:56
 */
@I18nResource("/META-INF/clougence/i18n/ob-ui-editor-table")
public interface ObDsI18nKeys extends MyDsI18nKeys, Ora18nKeys {

    String EDITOR_TABLEINFO_ROWFORMAT_DEFAULT_LABEL   = "UI_EDITOR_TABLEINFO_ROWFORMAT_DEFAULT_LABEL";
    String EDITOR_TABLEINFO_ROWFORMAT_CONDENSED_LABEL = "UI_EDITOR_TABLEINFO_ROWFORMAT_CONDENSED_LABEL";
}
