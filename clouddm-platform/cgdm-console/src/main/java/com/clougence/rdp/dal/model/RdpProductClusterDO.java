package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.dal.enumeration.RdpProduct;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/5
 **/
@Getter
@Setter
@TableName(value = "rdp_product_cluster")
public class RdpProductClusterDO {

    @TableId(type = IdType.AUTO)
    private Long       id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date       gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date       gmtModified;

    private RdpProduct product;

    private String     productVersion;

    private String     clusterName;

    private String     clusterDesc;

    private String     clusterCode;

    private String     apiAddr;
}
