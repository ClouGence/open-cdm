package com.clougence.rdp.dal.model.authcode;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.constant.ApplyCollectType;

import lombok.Data;

@Data
@TableName(value = "rdp_apply_collect_info")
public class RdpApplyCollectInfoDO {

    @TableId(type = IdType.AUTO)
    private Long             id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;

    private Date             reportTime;

    private Long             applyId;

    private Long             jobId;

    private Long             taskId;

    /**
     * job link type
     * eg: MYSQL_MYSQL,MYSQL_ORACLE
     */
    private String           linkType;

    private String           srcDsType;

    private String           dstDsType;

    private ApplyCollectType collectName;

    private String           collectValue;

}
