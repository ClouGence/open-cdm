package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;
import com.clougence.clouddm.console.web.dal.model.DmMetaInformationCacheDO;

public interface DmMetaInformationCacheMapper extends BaseMapper<DmMetaInformationCacheDO> {

    DmMetaInformationCacheDO queryCache(@Param("puid") String puid, @Param("dsId") Long dsId, @Param("path") String path, @Param("type") MetaInformationType type);

    void insertOrUpdate(@Param("puid") String puid, @Param("dsId") Long dsId, @Param("path") String path, @Param("type") MetaInformationType type,
                        @Param("context") String context);

    void deleteByPath(Long dsId, String path, MetaInformationType type, Date time);

    void deleteByPathLike(Long dsId, String path, Date time);

    int deleteBeforeDate(Date date);
}
