package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.clouddm.console.web.constants.I18nDmLabelKeys;
import com.clougence.clouddm.sdk.service.secrules.SensitiveMode;

import lombok.Getter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
public enum RuleSensitiveMode {

    ROW(I18nDmLabelKeys.SEC_SEN_MODE_ROW, SensitiveMode.Row),
    VALUE(I18nDmLabelKeys.SEC_SEN_MODE_VALUE, SensitiveMode.Value);

    private final String        i18nKey;
    private final SensitiveMode senMode;

    RuleSensitiveMode(I18nDmLabelKeys i18nKey, SensitiveMode mode){
        this.i18nKey = i18nKey.name();
        this.senMode = mode;
    }
}
