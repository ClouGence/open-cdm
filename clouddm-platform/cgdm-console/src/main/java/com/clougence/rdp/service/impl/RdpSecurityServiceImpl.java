/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.rdp.service.impl;

import java.text.MessageFormat;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsBlobResourceDO;
import com.clougence.rdp.service.RdpSecurityService;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/6/19 12:09
 */
@Service
@Slf4j
public class RdpSecurityServiceImpl implements RdpSecurityService {
    @Resource
    private DataSourceDal datasourceDal;

    @Override
    public byte[] querySecurityFile(String instanceId, ResourceType ownerType, SecurityFileType fileType) {
        if (ownerType != ResourceType.DATASOURCE) {
            throw new IllegalArgumentException("not supported owner type:" + ownerType);
        }

        DmDsBlobResourceDO r = datasourceDal.blobResourceMapper().queryByIdentify(instanceId, ownerType, fileType);
        return r.getContent();
    }

    @Override
    public String genSecurityFileRelatePath(String instanceId, String simpleFileName) {
        if (StringUtils.isBlank(simpleFileName)) {
            throw new IllegalArgumentException("security file name can not be empty.");
        }

        return MessageFormat.format(SECURITY_FILE_RELATED_PATH_FORMAT, instanceId, simpleFileName);
    }
}
