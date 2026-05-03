package com.clougence.rdp.service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;

/**
 * @author bucketli 2020/6/19 12:05
 */
public interface RdpSecurityService {

    /**
     * need add user.home parent path
     */
    String SECURITY_FILE_RELATED_PATH_FORMAT = "key/{0}/{1}";

    byte[] querySecurityFile(String instanceId, ResourceType ownerType, SecurityFileType fileType);

    String genSecurityFileRelatePath(String instanceId, String simpleFileName);
}
