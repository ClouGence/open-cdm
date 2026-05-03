package com.clougence.rdp.controller.model.vo;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.util.RdpI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/7/11 12:50:14
 */
@Setter
@Getter
public class ConnectTypeVO {

    private DataSourceType dataSourceType;

    private ConnectType    connectType;

    private String         i18nName;

    private boolean        defaultCheck;

    public ConnectTypeVO(ConnectType connectType, boolean defaultCheck){
        this.connectType = connectType;
        this.i18nName = RdpI18nUtils.getMessage(connectType.getI18nKey());
        this.defaultCheck = defaultCheck;
        this.dataSourceType = connectType.getDsType();
    }
}
