/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.platform.dal.model.approval;

import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.platform.dal.handler.EnumOfCode;
import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * @author wanshao create time is 2021/1/19
 **/
@Getter
public enum ApprovalType implements EnumOfCode<ApprovalType> {

    Internal(ApprovalProvider.Internal, "APPROVAL_INTERNAL"),
    DingTalk(ApprovalProvider.DingTalk, "APPROVAL_DINGTALK"),
    Wechat(ApprovalProvider.Wechat, "APPROVAL_WECHAT"),
    Feishu(ApprovalProvider.Feishu, "APPROVAL_FEISHU"),
    Custom(ApprovalProvider.Custom, "APPROVAL_CUSTOM"),;

    private final ApprovalProvider providerType;
    private final String           i18nKey;

    ApprovalType(ApprovalProvider providerType, String i18nKey){
        this.providerType = providerType;
        this.i18nKey = i18nKey;
    }

    public static ApprovalType getByName(String name) {
        if (name == null) {
            return null;
        }

        for (ApprovalType value : ApprovalType.values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }

        if (StringUtils.equalsIgnoreCase("SELFMAKE", name)) {
            return ApprovalType.Internal; // SELFMAKE is old data.
        }

        return null;
    }

    public static ApprovalType valueOfProvider(ApprovalProvider type) {
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
    public ApprovalType valueOfCode(String codeString) {
        ApprovalType result = getByName(codeString);
        if (result != null) {
            return result;
        }

        throw new EnumConstantNotPresentException(ApprovalType.class, codeString);
    }
}
