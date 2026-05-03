package com.clougence.rdp.dal.model.authcode;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

@Data
@Deprecated
@TableName(value = "rdp_apply_code_info")
public class RdpApplyCodeInfoDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    private Date   reportTime;

    /**
     * clusterIp,clusterMacAddress,clusterHardwareUuid bind unique cluster
     */
    private String clusterIp;

    private String clusterMacAddress;

    private String clusterHardwareUuid;

    /**
     * json:
     *  ApplyInfoForCc
     */
    private String info;

    /**
     * CloudCanal,CloudDM
     */
    private String applyType;

}
