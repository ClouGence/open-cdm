package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.clouddm.console.web.constants.I18nDmLabelKeys;
import com.clougence.clouddm.sdk.scm.ScmProviderNames;

import lombok.Getter;

/**
 * @author mode create time is 2019/12/11 10:10 下午 finish
 */
@Getter
public enum ScmType {

    Gitee(ScmProviderNames.Gitee, I18nDmLabelKeys.DEVOPS_PROVIDER_GITEE.name(), false),
    Github(ScmProviderNames.Github, I18nDmLabelKeys.DEVOPS_PROVIDER_GITHUB.name(), false),;

    private final ScmProviderNames providerType;
    private final String             i18nKey;
    private final boolean            supportCustom;

    ScmType(ScmProviderNames providerType, String i18nKey, boolean supportCustom){
        this.providerType = providerType;
        this.i18nKey = i18nKey;
        this.supportCustom = supportCustom;
    }
}
