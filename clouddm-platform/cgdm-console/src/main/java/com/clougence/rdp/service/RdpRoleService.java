package com.clougence.rdp.service;

import java.util.List;
import java.util.Set;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.rdp.controller.model.fo.role.CreateRoleFO;
import com.clougence.rdp.controller.model.fo.role.DeleteRoleFO;
import com.clougence.rdp.controller.model.fo.role.UpdateRoleFO;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.rdp.service.model.AddRoleMO;

/**
 * @author bucketli 2021/1/9 12:21
 */
public interface RdpRoleService {

    Set<String> getInnerRoleLabel(String roleName);

    List<RdpRoleDO> listRoleExcludeByName(String puid, List<String> name);

    List<RdpRoleDO> listRoleByUID(String puid);

    RdpRoleDO fetchRoleById(long roleId);

    AddRoleMO createRole(String puid, CreateRoleFO createRoleFO);

    ResWebData<Boolean> updateRole(String puid, UpdateRoleFO updateRoleFO);

    ResWebData<Boolean> deleteRole(String puid, DeleteRoleFO deleteRoleFO);

    void repairRoleForUser(String uid);
}
