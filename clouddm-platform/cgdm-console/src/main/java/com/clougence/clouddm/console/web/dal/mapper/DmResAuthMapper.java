package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmResAuthDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

/**
 * @author mode create time is 2021/1/5
 **/
public interface DmResAuthMapper extends BaseMapper<DmResAuthDO> {

    List<DmResAuthDO> listByKind(String ownerUid, AuthKind authKind);

    List<DmResAuthDO> listWithoutLabels(String ownerUid, AuthKind authKind);

    List<DmResAuthDO> listByResIds(List<Long> resIds, String ownerUid, AuthKind authKind);

    List<DmResAuthDO> listByResId(Long resId, String ownerUid, AuthKind authKind);

    long queryAuthCountByUser(String ownerUid);

    void updateAuthById(long id, String resAuthLabel, Date startTime, Date endTime);

    void deleteByRes(long resId, AuthKind authKind);

    void deleteByPrefix(String prefixPath, String kindType);

    void insertOne(@Param("item") DmResAuthDO dsAuthDO);
}
