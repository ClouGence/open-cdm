/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.platform.dal.mapper.execution;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlScriptDO;

public interface DmExecSqlScriptMapper extends BaseMapper<DmExecSqlScriptDO> {

    IPage<DmExecSqlScriptDO> listByOwner(Page<?> page, @Param("ownerUid") String ownerUid,
                                         @Param("keyword") String keyword);

    DmExecSqlScriptDO detail(@Param("scriptId") long scriptId, @Param("ownerUid") String ownerUid);

    int updateByVersion(@Param("scriptId") long scriptId, @Param("ownerUid") String ownerUid,
                        @Param("version") long version, @Param("name") String name,
                        @Param("dsType") String dsType, @Param("fileUri") String fileUri,
                        @Param("gmtModified") Date gmtModified);

    int markDeleting(@Param("scriptId") long scriptId, @Param("ownerUid") String ownerUid,
                     @Param("version") long version, @Param("gmtModified") Date gmtModified);

    IPage<DmExecSqlScriptDO> listDeleting(Page<?> page, @Param("expireBefore") Date expireBefore);

    int deleteDeleting(@Param("scriptId") long scriptId);

    List<String> listFileUris();
}
