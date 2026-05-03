package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.constant.KvConfValType;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.dal.enumeration.ConfBelong;

import lombok.Data;

/**
 * @author bucketli 2020/11/9 13:23
 */
@Data
@TableName(value = "rdp_user_kv_base_config")
public class RdpUserKvBaseConfigDO {

    @TableId(type = IdType.AUTO)
    private Long              id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtModified;

    private String            uid;

    private String            configName;

    private String            configValue;

    private String            defaultValue;

    private String            valueRange;

    private boolean           readOnly;

    private UserConfigTagType userConfigTagType;

    private ConfBelong        confBelong;

    private KvConfValType     confValType;

    private boolean           isSecret;

    private String            descKey;
}
