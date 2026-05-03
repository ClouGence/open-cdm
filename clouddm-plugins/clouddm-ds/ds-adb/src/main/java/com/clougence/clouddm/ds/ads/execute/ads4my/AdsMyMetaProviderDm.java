package com.clougence.clouddm.ds.ads.execute.ads4my;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.jdbc.extractor.SingleRowResultSetExtractor;
import com.clougence.utils.jdbc.mapper.StringMapRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Adb For MySql 3.0 metadata
 * <li>https://dev.mysql.com/doc/refman/8.0/en/information-schema.html</li>
 * <li>https://help.aliyun.com/document_detail/197326.html</li>
 *
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class AdsMyMetaProviderDm extends MyMetaProviderDm implements MetaDataService {

    public AdsMyMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new AdsMyMetaProviderUtils();
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            Map<String, String> mapObject;
            try (PreparedStatement ps = conn.prepareStatement("select adb_version()")) {
                try (ResultSet resultSet = ps.executeQuery()) {
                    Map<String, Integer> extractColumn = extractColumn(resultSet.getMetaData());
                    mapObject = new SingleRowResultSetExtractor<>(new StringMapRowMapper(extractColumn)).extractData(resultSet);
                } catch (Exception e) {
                    return "Unknown";
                }
            }

            if (mapObject == null) {
                return "Unknown";
            } else {
                return mapObject.get("source_version");
            }
        }
    }

    public List<Value> selectMaterialized(String schema) {
        return Collections.emptyList();
    }

    public List<RdbTable> loadMaterialized(String schema, List<String> views) {
        return Collections.emptyList();
    }
}
