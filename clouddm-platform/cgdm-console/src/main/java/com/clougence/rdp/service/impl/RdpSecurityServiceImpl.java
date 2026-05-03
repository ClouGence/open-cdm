package com.clougence.rdp.service.impl;

import java.text.MessageFormat;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.rdp.dal.mapper.RdpBlobResourceMapper;
import com.clougence.rdp.dal.model.RdpBlobResourceDO;
import com.clougence.rdp.service.RdpSecurityService;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/6/19 12:09
 */
@Service
@Slf4j
public class RdpSecurityServiceImpl implements RdpSecurityService {

    @Resource
    private RdpBlobResourceMapper resourceMapper;

    @Override
    public byte[] querySecurityFile(String instanceId, ResourceType ownerType, SecurityFileType fileType) {
        if (ownerType != ResourceType.DATASOURCE) {
            throw new IllegalArgumentException("not supported owner type:" + ownerType);
        }

        RdpBlobResourceDO r = resourceMapper.queryByIdentify(instanceId, ownerType, fileType);
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
