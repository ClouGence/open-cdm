package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.clougence.rdp.constant.KvConfValType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class GoogleDriveExtraConfig extends FileExtraConfig {

    @DsConfigDef(name = "projectId", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_PROJECT_ID, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String projectId;

    @DsConfigDef(name = "privateKeyId", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_SERVICE_ACCOUNT_PRIVATE_KEY_ID, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String privateKeyId;

    @DsConfigDef(name = "privateKey", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_SERVICE_ACCOUNT_PRIVATE_KEY, isSecret = true, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String privateKey;

    @DsConfigDef(name = "clientEmail", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_SERVICE_ACCOUNT_EMAIL, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String clientEmail;

    @DsConfigDef(name = "clientId", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_SERVICE_ACCOUNT_ID, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String clientId;
}
