package com.clougence.rdp.controller.model.vo;

import java.util.Date;

import com.clougence.rdp.dal.enumeration.RdpProduct;
import com.clougence.rdp.dal.model.RdpProductClusterDO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2023/11/23 16:42:46
 */
@Getter
@Setter
public class RdpProductClusterVO {

    private Long       id;

    private Date       gmtCreate;

    private Date       gmtModified;

    private RdpProduct product;

    private String     productVersion;

    private String     clusterName;

    private String     clusterDesc;

    private String     clusterCode;

    private String     apiAddr;

    public void fromDO(RdpProductClusterDO clusterDO) {
        this.id = clusterDO.getId();
        this.gmtCreate = clusterDO.getGmtCreate();
        this.gmtModified = clusterDO.getGmtModified();
        this.product = clusterDO.getProduct();
        this.productVersion = clusterDO.getProductVersion();
        this.clusterName = clusterDO.getClusterName();
        this.clusterDesc = clusterDO.getClusterDesc();
        this.clusterCode = clusterDO.getClusterCode();
        this.apiAddr = clusterDO.getApiAddr();
    }
}
