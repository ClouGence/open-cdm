package com.clougence.rdp.controller.model.vo;

import com.clougence.rdp.constant.KvConfValType;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.util.RdpI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zylicfc
 */
@Getter
@Setter
public class RdpUserConfigVO {

    private String            uid;

    private String            configName;

    private String            configValue;

    private String            defaultValue;

    private String            valueRange;

    private String            description;

    private boolean           readOnly;

    private UserConfigTagType userConfigTagType;

    private String            i18nOfTagType;

    private String            confBelong;

    private KvConfValType     confValType;

    private boolean           isSecret;

    private boolean           needCreated;

    public void convertFromDO(RdpUserKvBaseConfigDO config) {
        if (config.isSecret()) {
            this.setSecret(config.isSecret());
        } else {
            this.setConfigValue(config.getConfigValue());
        }

        this.setDescription(RdpI18nUtils.getMessage(config.getDescKey()));
        this.setConfigName(config.getConfigName());
        this.setUserConfigTagType(config.getUserConfigTagType());
        if (config.getUserConfigTagType() != null) {
            this.i18nOfTagType = RdpI18nUtils.getMessage("USER_CONFIG_TAG_" + config.getUserConfigTagType().name());
        }

        this.setConfBelong(config.getConfBelong().getCloudAliasName());
        this.setUid(config.getUid());
        this.setDefaultValue(config.getDefaultValue());
        this.setReadOnly(config.isReadOnly());
        this.setValueRange(config.getValueRange());

        if (config.getConfValType() != null) {
            this.setConfValType(config.getConfValType());
        } else {
            this.setConfValType(KvConfValType.TEXT);
        }
    }
}
