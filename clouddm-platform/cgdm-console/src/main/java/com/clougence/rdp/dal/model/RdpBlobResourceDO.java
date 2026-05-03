package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;

import lombok.Data;

/**
 * @author bucketli 2020/6/19 10:56
 */
@Data
@TableName(value = "rdp_blob_resource")
public class RdpBlobResourceDO {

    @TableId(type = IdType.AUTO)
    private Long             id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;

    /**
     * e.g., datasource instanceId(string) ,job instanceId(number) or other need
     * blob resource entity's id.
     */
    private String           instanceId;

    /**
     * e.g., datasource instance id, job instance id ,task instance id .etc
     */
    private String           ownerName;

    private ResourceType     ownerType;

    private SecurityFileType blobType;

    private byte[]           content;
}
