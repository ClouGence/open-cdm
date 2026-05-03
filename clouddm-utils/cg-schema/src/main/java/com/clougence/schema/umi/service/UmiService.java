package com.clougence.schema.umi.service;

import java.sql.SQLException;

import com.clougence.schema.DsType;

/**
 * Unified Meta Information Service
 * 
 * @author mode 2020/12/8 15:21
 */
public interface UmiService {

    DsType getDataSourceType();

    String getVersion() throws SQLException;
}
