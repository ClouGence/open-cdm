/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.platform.dal.mapper.system;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.system.DmSysAttachmentDO;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;

public interface DmSysAttachmentMapper extends BaseMapper<DmSysAttachmentDO> {

    DmSysAttachmentDO selectByIdForUpdate(@Param("id") long id);

    DmSysAttachmentDO selectConfirmedByApprovalId(@Param("approvalId") long approvalId);

    List<DmSysAttachmentDO> listEditingByTypeAndFileName(@Param("attachmentType") SysAttachmentType attachmentType, @Param("fileName") String fileName);

    List<DmSysAttachmentDO> listExpiredEditing(@Param("before") Date before, @Param("limit") int limit);

    int deleteExpiredEditing(@Param("id") long id, @Param("before") Date before);

    int touchEditing(@Param("id") long id, @Param("ownerUid") String ownerUid, @Param("before") Date before);

    int lock(@Param("id") long id, @Param("approvalId") long approvalId, @Param("ownerUid") String ownerUid, @Param("before") Date before);
}
