package com.clougence.clouddm.ds.redis.execute;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author mode 2021/11/16 09:55:55
 */
@Slf4j
public class RedisMetaProviderUtils {

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        Properties properties = convertServerInfo(rs);
        String dbStr = properties.getProperty("databases");
        int databases = StringUtils.isBlank(dbStr) ? 0 : Integer.parseInt(dbStr);

        List<Value> result = new ArrayList<>();
        for (int i = 0; i < databases; i++) {
            RdbValue schema = new RdbValue();
            schema.setValue(String.valueOf(i));
            schema.setUmiType(UmiTypes.Schema);
            result.add(schema);
        }
        return result;
    }

    public static Properties convertServerInfo(ResultSet rs) throws SQLException {
        Properties result = new Properties();
        while (rs.next()) {
            String name = rs.getString("NAME");
            String value = rs.getString("VALUE");
            result.put(name, value);
        }
        return result;
    }

    public static String keyTypeDesc(String oriType) {
        switch (oriType.toLowerCase()) {
            case "string":
                return "String";
            case "list":
                return "List";
            case "set":
                return "Set";
            case "zset":
                return "ZSet";
            case "hash":
                return "Hash";
            case "none":
            default:
                return "Null";
        }
    }
}
