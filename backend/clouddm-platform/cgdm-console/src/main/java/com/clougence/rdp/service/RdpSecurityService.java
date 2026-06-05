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
