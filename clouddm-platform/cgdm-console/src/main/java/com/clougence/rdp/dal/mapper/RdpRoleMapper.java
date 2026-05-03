package com.clougence.rdp.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpRoleDO;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface RdpRoleMapper extends BaseMapper<RdpRoleDO> {

    List<RdpRoleDO> queryByOwnerUid(String uid);

    List<RdpRoleDO> queryByRoleName(String uid, String roleName);

    int updateRole(long roleId, String roleName, String newLabels);

    void updateInnerRoleByName(String roleName, String newLabels);

    List<RdpRoleDO> listByIds(List<Long> ids);
}
