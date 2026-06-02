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
package com.clougence.clouddm.init.component.fixtasks;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RdpFixUserRole {

    @Resource
    private RdpUserService userService;
    @Resource
    private RdpRoleService roleService;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void init() {
        this.roleService.init();

        List<DmAuthUserDO> primaryUsers = this.userService.listPrimaryUser();
        for (DmAuthUserDO user : primaryUsers) {
            log.info("RdpFixUserRole: repairRoleForUser " + user.getUid());
            this.roleService.repairRoleForUser(user.getUid());
        }
    }
}
