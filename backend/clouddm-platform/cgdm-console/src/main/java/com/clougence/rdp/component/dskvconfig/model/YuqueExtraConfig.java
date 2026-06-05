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
package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.rdp.enumeration.CookieInfo;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author chunlin create time is 2025/6/13
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class YuqueExtraConfig extends FileExtraConfig {

    @DsConfigDef(name = "repository", descKey = I18nDsConfigMsgKeys.DS_CONFIG_YUQUE_REPOSITORY, readOnly = false, valueAdvance = "Yuque repository slug (e.g., '<repository>' from www.yuque.com/<repository>)")
    private String     repository;

    @DsConfigDef(name = "publicKey", defaultValue = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfwyOyncSrUTmkaUPsXT6UUdXxTQ6a0wgPShvebfwq8XeNj575bUlXxVa/ExIn4nOUwx6iR7vJ2fvz5Ls750D051S7q70sevcmc8SsBNoaMQtyF/gETPBSsyWv3ccBJFrzZ5hxFdlVUfg6tXARtEI8rbIHsu6TBkVjk+n1Pw/ihQIDAQAB", descKey = I18nDsConfigMsgKeys.DS_CONFIG_YUQUE_PUBLIC_KEY, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String     publicKey;

    @DsConfigDef(name = "cookie", isSecret = true, descKey = I18nDsConfigMsgKeys.DS_CONFIG_YUQUE_COOKIE, readOnly = false, kvConfWebOp = KvConfValType.TEXT, valueAdvance = "JSON format: { \"expire\": <timestamp>, \"data\": \"<cookie string>\" }")
    private String     cookie;

    @DsConfigDef(name = "cookieExpire", defaultValue = "2419200000", descKey = I18nDsConfigMsgKeys.DS_CONFIG_YUQUE_COOKIE_EXPIRE, readOnly = false, kvConfWebOp = KvConfValType.TEXT, valueAdvance = "1 - ")
    private Long       cookieExpire;

    @DsConfigDef(name = "userAgent", defaultValue = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/20G81 YuqueMobileApp/1.0.2 (AppBuild/650 Device/Phone Locale/zh-cn Theme/light YuqueType/public)", descKey = I18nDsConfigMsgKeys.DS_CONFIG_YUQUE_USER_AGENT, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String     userAgent;

    private CookieInfo cookieInfo;

    public void deserialize() {
        if (StringUtils.isNotBlank(cookie)) {
            try {
                cookieInfo = new ObjectMapper().readValue(cookie, new TypeReference<CookieInfo>() {});
            } catch (Exception e) {
                throw new RuntimeException("Process yuque extra config error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }
    }
}
