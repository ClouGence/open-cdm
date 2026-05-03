package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

@Data
@TableName(value = "rdp_product")
public class RdpProductDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    private String productType;

    private String productVersion;

    private String pkgMd5;

    private String ossBucket;

    private String ossObjectName;

    private String ossEndPoint;

    private String ossDownloadSite;

    private String s3Bucket;

    private String s3ObjectName;

    private String s3Region;

    private String s3DownloadSite;

    private String productVersionType;
}
