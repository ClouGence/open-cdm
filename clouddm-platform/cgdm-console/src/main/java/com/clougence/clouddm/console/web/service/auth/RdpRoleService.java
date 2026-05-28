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
package com.clougence.clouddm.console.web.service.auth;

import java.util.List;
import java.util.Set;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.model.fo.role.CreateRoleFO;
import com.clougence.clouddm.console.web.model.fo.role.DeleteRoleFO;
import com.clougence.clouddm.console.web.model.fo.role.UpdateRoleFO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.rdp.service.model.AddRoleMO;

/**
 * @author bucketli 2021/1/9 12:21
 */
public interface RdpRoleService {

    Set<String> getInnerRoleLabel(String roleName);

    List<DmAuthRoleDO> listRoleExcludeByName(String puid, List<String> name);

    List<DmAuthRoleDO> listRoleByUID(String puid);

    DmAuthRoleDO fetchRoleById(long roleId);

    AddRoleMO createRole(String puid, CreateRoleFO createRoleFO);

    ResWebData<Boolean> updateRole(String puid, UpdateRoleFO updateRoleFO);

    ResWebData<Boolean> deleteRole(String puid, DeleteRoleFO deleteRoleFO);

    void repairRoleForUser(String uid);
}
