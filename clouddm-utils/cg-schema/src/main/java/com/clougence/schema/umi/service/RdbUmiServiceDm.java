package com.clougence.schema.umi.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

/**
 * Unified Meta Information Service
 * 
 * @author mode 2020/12/8 15:21
 */
public interface RdbUmiServiceDm extends UmiService {

    List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException;

    Value detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException;

    List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException;

    Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException;

    Map<String, List<RdbColumn>> loadColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames) throws SQLException;
}
