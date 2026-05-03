package com.clougence.clouddm.ds.oceanbase.execute.obformysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-01-22
 */
@Slf4j
public class ObForMySQLMetaProviderDm extends MyMetaProviderDm implements MetaDataService {

    public ObForMySQLMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new ObForMySQLMetaProviderUtils();
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement("select version()"); ResultSet resultSet = ps.executeQuery()) {
            return ((SpecialValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }
}
