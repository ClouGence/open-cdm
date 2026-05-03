package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsTagDO;

/**
 * @author mode create time is 2019/12/12 9:25 下午
 **/
public interface DmDsTagMapper extends BaseMapper<DmDsTagDO> {

    DmDsTagDO getByDsAndUser(long dsId, String uid);

    List<DmDsTagDO> listByDsAndUser(List<Long> dsIds, String uid);

    int deleteByDsAndUser(long dsId, String uid);

    int insertByDsAndUser(long dsId, String uid, String remark);

    int updateByDsAndUser(long dsId, String uid, String remark);

    int updateForDesktop(String uid);
}
