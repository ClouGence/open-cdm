package com.clougence.rdp.controller.model.vo;

import java.util.Map;

import com.clougence.rdp.dal.enumeration.RdpProduct;

import lombok.Data;

/**
 * @author wanshao create time is 2020/4/16
 **/
@Data
public class RdpGlobalSettingsVO {

    // for Product
    private boolean             productTrial;
    private String              deploySite;
    private String              deployEnv;
    private String              deployMode;

    // for Auth
    private boolean             authVerifyCodeEnable;
    private boolean             authOpPassword;

    private boolean             enableWaterMark;
    private boolean             enableProductCluster;
    private RdpProduct          defaultProduct;

    private Map<String, Object> features;

    private Integer             maxExportSize;
}
