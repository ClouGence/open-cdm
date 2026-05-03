package com.clougence.rdp.controller.model.vo;

import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.rdp.constant.KvConfValType;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.util.RdpI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/8/10 11:26:59
 */
@Getter
@Setter
public class DefaultDsKvConfigVO {

    private String        configName;

    private DsConfigGroup configGroup;

    private String        description;

    private boolean       valueRequire;

    private String        defaultValue;

    private String        valueAdvance;

    private KvConfValType confValType;

    public void convertFromDO(RdpDsKvBaseConfigDO config) {
        this.description = RdpI18nUtils.getMessage(config.getDescKey());
        this.configName = config.getConfigName();
        this.configGroup = config.getConfigGroup();
        this.valueRequire = config.isValueRequire();
        this.defaultValue = config.getDefaultValue();
        this.valueAdvance = config.getValueAdvance();

        if (config.getConfValType() != null) {
            this.setConfValType(config.getConfValType());
        } else {
            this.setConfValType(KvConfValType.TEXT);
        }
    }
}
