package com.clougence.rdp.service.openapi.model;

import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.rdp.controller.model.vo.DsKvConfigVO;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: baili
 * @Date: 2023/04/13/19:57
 * @Description:
 */
@Getter
@Setter
public class ApiDsKvConfigVo {

    private Long          id;

    private String        configName;

    private DsConfigGroup configGroup;

    private String        description;

    private boolean       valueRequire;

    private String        valueValidRegex;

    private String        configValue;

    private String        defaultValue;

    private String        valueAdvance;

    private boolean       readOnly;

    private boolean       isSecret;

    private boolean       needCreated;

    public void convertFromDsKvConfigVO(DsKvConfigVO configVO) {
        this.id = configVO.getId();
        this.configName = configVO.getConfigName();
        this.configGroup = configVO.getConfigGroup();
        this.description = configVO.getDescription();
        this.valueRequire = configVO.isValueRequire();
        this.valueValidRegex = configVO.getValueValidRegex();
        this.configValue = configVO.getConfigValue();
        this.defaultValue = configVO.getDefaultValue();
        this.valueAdvance = configVO.getValueAdvance();
        this.readOnly = configVO.isReadOnly();
        this.isSecret = configVO.isSecret();
        this.needCreated = configVO.isNeedCreated();
    }
}
