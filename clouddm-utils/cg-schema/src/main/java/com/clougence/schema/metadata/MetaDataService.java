package com.clougence.schema.metadata;

import java.sql.SQLException;

import com.clougence.schema.DsType;
import com.clougence.schema.SchemaFramework;
import com.clougence.utils.StringUtils;

/**
 * 元信息服务
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public interface MetaDataService {

    String getVersion() throws SQLException;

    static MainVersion getMainVersion(String version, DsType dsType) {
        if (StringUtils.isBlank(version) || dsType == null) {
            return new Version(dsType, version);
        }

        return SchemaFramework.getMainVersion(dsType, version);
    }
}
