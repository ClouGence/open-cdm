package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-05-30 15:23
 */
@Getter
@Setter
@TableName(value = "rdp_env_param")
public class RdpEnvParamDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    private long   envId;

    private String configKey;

    private String configValue;

    private String primaryUid;
}
