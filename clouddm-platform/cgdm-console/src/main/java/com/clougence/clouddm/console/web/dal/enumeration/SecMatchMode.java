package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.clouddm.console.web.constants.I18nDmLabelKeys;
import com.clougence.clouddm.sdk.service.secrules.MatchMode;

import lombok.Getter;

/**
 * @author mode 2021/1/7 19:16
 */
@Getter
public enum SecMatchMode {

    EXACT(MatchMode.EXACT, I18nDmLabelKeys.SEC_SEN_MATCH_EXACT),
    PREFIX(MatchMode.PREFIX, I18nDmLabelKeys.SEC_SEN_MATCH_PREFIX),
    SUFFIX(MatchMode.SUFFIX, I18nDmLabelKeys.SEC_SEN_MATCH_SUFFIX),
    INCLUDE(MatchMode.INCLUDE, I18nDmLabelKeys.SEC_SEN_MATCH_INCLUDE);

    private final String    i18nKey;
    private final MatchMode matchMode;

    SecMatchMode(MatchMode matchMode, I18nDmLabelKeys i18nKey){
        this.matchMode = matchMode;
        this.i18nKey = i18nKey.name();
    }
}
