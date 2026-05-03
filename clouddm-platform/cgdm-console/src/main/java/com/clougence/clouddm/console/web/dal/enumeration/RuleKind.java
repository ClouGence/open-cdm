package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.clouddm.console.web.constants.I18nDmLabelKeys;

import lombok.Getter;

/**
 * @Author: Ekko
 * @Date: 2024-07-10 14:59
 */
@Getter
public enum RuleKind {

    QUERY(I18nDmLabelKeys.SEC_SEN_KIND_QUERY),
    SENSITIVE(I18nDmLabelKeys.SEC_SEN_KIND_SENSITIVE);

    private final String i18nKey;

    RuleKind(I18nDmLabelKeys i18nKey){
        this.i18nKey = i18nKey.name();
    }
}
