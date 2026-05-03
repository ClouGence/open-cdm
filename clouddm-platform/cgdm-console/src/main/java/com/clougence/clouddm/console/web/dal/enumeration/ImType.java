package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.clouddm.console.web.constants.I18nDmLabelKeys;
import com.clougence.clouddm.sdk.messenger.MsgProviderType;

import lombok.Getter;

/**
 * @author mode create time is 2019/12/11 10:10 下午 finish
 */
@Getter
public enum ImType {

    DingTalk(MsgProviderType.DingTalk, I18nDmLabelKeys.DEVOPS_PROVIDER_DINGTALK.name()),
    Wechat(MsgProviderType.Wechat, I18nDmLabelKeys.DEVOPS_PROVIDER_WECHAT.name()),
    Feishu(MsgProviderType.Feishu, I18nDmLabelKeys.DEVOPS_PROVIDER_FEISHU.name()),;

    private final MsgProviderType providerType;
    private final String                i18nKey;

    ImType(MsgProviderType providerType, String i18nKey){
        this.providerType = providerType;
        this.i18nKey = i18nKey;
    }
}
