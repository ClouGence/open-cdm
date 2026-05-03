package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsStatisticsDO;

/**
 * @author mode create time is 2019/12/12 9:26 下午
 **/
public interface DmDsStatisticsMapper extends BaseMapper<DmDsStatisticsDO> {

    void updateCounts(@Param("dsID") long dsID, @Param("dsType") String dsType);

    List<DmDsStatisticsDO> listStatistics();

    DmDsStatisticsDO getByDsId(@Param("dsID") long dsID);

    void initStatisticsDO(DmDsStatisticsDO dsStatisticsDO);
}
