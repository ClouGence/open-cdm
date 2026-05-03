package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

/**
 * @author wanshao create time is 2019/12/11 10:10 下午
 **/

@Data
@Deprecated
@TableName(value = "rdp_user_download")
public class RdpUserDownloadDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    private String uid;

    private String username;

    private String company;

    private String productType;

    private String productVersion;

    private String productVersionType;
}
