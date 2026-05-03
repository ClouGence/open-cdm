package com.clougence.rdp.dal.enumeration;

import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.rdp.constant.I18nRdpLabelKeys;
import com.clougence.rdp.dal.handler.EnumOfCode;
import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * @author wanshao create time is 2021/1/19
 **/
@Getter
public enum RdpApprovalType implements EnumOfCode<RdpApprovalType> {

    Internal(ApprovalProvider.Internal, I18nRdpLabelKeys.APPROVAL_INTERNAL.name()),
    DingTalk(ApprovalProvider.DingTalk, I18nRdpLabelKeys.APPROVAL_DINGTALK.name()),
    Wechat(ApprovalProvider.Wechat, I18nRdpLabelKeys.APPROVAL_WECHAT.name()),
    Feishu(ApprovalProvider.Feishu, I18nRdpLabelKeys.APPROVAL_FEISHU.name()),
    Custom(ApprovalProvider.Custom, I18nRdpLabelKeys.APPROVAL_CUSTOM.name()),;

    private final ApprovalProvider providerType;
    private final String                  i18nKey;

    RdpApprovalType(ApprovalProvider providerType, String i18nKey){
        this.providerType = providerType;
        this.i18nKey = i18nKey;
    }

    public static RdpApprovalType getByName(String name) {
        if (name == null) {
            return null;
        }

        for (RdpApprovalType value : RdpApprovalType.values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }

        if (StringUtils.equalsIgnoreCase("SELFMAKE", name)) {
            return RdpApprovalType.Internal; // SELFMAKE is old data.
        }

        return null;
    }

    public static RdpApprovalType valueOfProvider(ApprovalProvider type) {
        switch (type) {
            case DingTalk:
                return DingTalk;
            case Wechat:
                return Wechat;
            case Custom:
                return Custom;
            case Feishu:
                return Feishu;
            case Internal:
                return Internal;
            default:
                throw new UnsupportedOperationException("Unsupported type " + type);
        }
    }

    @Override
    public RdpApprovalType valueOfCode(String codeString) {
        RdpApprovalType result = getByName(codeString);
        if (result != null) {
            return result;
        }

        throw new EnumConstantNotPresentException(RdpApprovalType.class, codeString);
    }
}
