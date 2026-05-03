package com.clougence.rdp.component.dskvconfig.model;

import static com.clougence.rdp.constant.I18nDsConfigMsgKeys.CONFIG_DATA_SOURCE_SSL_MODE;

import com.clougence.rdp.constant.DsConfigDef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class MySQLExtraConfig extends CommonDsExtraConfig {

    @DsConfigDef(name = "sslMode", defaultValue = "DISABLED", valueAdvance = "DISABLED/PREFERRED/REQUIRED/VERIFY_CA/VERIFY_IDENTITY", descKey = CONFIG_DATA_SOURCE_SSL_MODE, readOnly = false)
    private String sslMode;
}
