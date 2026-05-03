package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmQueryConstraintsDO;

public interface DmQueryConstraintsMapper extends BaseMapper<DmQueryConstraintsDO> {

    List<DmQueryConstraintsDO> selectAllByUid(String primaryUid, Long dsId, List<String> paths);
}
