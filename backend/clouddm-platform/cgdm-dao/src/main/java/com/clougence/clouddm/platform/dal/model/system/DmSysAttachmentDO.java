/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.platform.dal.model.system;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("dm_sys_attachment")
public class DmSysAttachmentDO {

    @TableId(type = IdType.AUTO)
    private Long                     id;
    private Date                     gmtCreate;
    private Date                     gmtModified;
    private String                   ownerUid;
    private Long                     approvalId;
    private SysAttachmentType        attachmentType;
    private SysAttachmentStatus      attachmentStatus;
    private String                   fileName;
    private Long                     fileSize;
    private String                   fileHash;
}
