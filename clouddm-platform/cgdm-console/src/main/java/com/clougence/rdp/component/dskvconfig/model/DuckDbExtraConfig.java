package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/5/29 16:55
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class DuckDbExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "motherDuckToken", descKey = I18nDsConfigMsgKeys.DS_CONFIG_MOTHER_DUCK_TOKEN, readOnly = false)
    private String motherDuckToken;
}
