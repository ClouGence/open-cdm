package com.clougence.rdp.dal.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface RdpResAuthMapper extends BaseMapper<RdpResAuthDO> {

    List<RdpResAuthDO> queryByPathLike(long resId, String ownerUid, AuthKind authKind, List<String> prefixList);

    List<RdpResAuthDO> queryByLikePath(long resId, String ownerUid, AuthKind authKind, String path);

    List<RdpResAuthDO> queryByPath(long resId, String ownerUid, AuthKind authKind, String path);

    void deleteByPath(long resId, String ownerUid, AuthKind authKind, String path);

    List<RdpResAuthDO> listByKind(String ownerUid, AuthKind authKind);

    List<RdpResAuthDO> listWithoutLabels(String ownerUid, AuthKind authKind);

    RdpResAuthDO queryByUniqueKey(long resId, String ownerUid, String resPath, AuthKind authKind);

    long queryAuthCountByUser(String ownerUid);

    void updateAuthById(long id, String resAuthLabel, Date startTime, Date endTime);

    void deleteByRes(long resId, AuthKind authKind);

    void deleteByUser(String ownerUid);

    void deleteByPrefix(String prefixPath, String kindType);

    void deleteByEndTimeExceed(Date endTime);
}
