package com.clougence.clouddm.ds.mongodb.execute;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

public class MongoMetaProviderUtils {

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue value = new RdbValue();
            value.setUmiType(UmiTypes.Schema);
            value.setValue(rs.getString("name"));
            result.add(value);
        }
        result.sort((o1, o2) -> ((RdbValue) o1).getValue().compareTo(((RdbValue) o2).getValue()));
        return result;
    }

    public static List<Value> convertCollections(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            String type = rs.getString("type");
            String name = rs.getString("name");
            if (!"collection".equals(type)) {
                continue;
            }
            RdbValue value = new RdbValue();
            value.setUmiType(UmiTypes.Table);
            value.setValue(name);
            result.add(value);
        }
        result.sort((o1, o2) -> ((RdbValue) o1).getValue().compareTo(((RdbValue) o2).getValue()));
        return result;
    }

    public static List<Value> convertViews(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            String type = rs.getString("type");
            if (!"view".equals(type)) {
                continue;
            }
            RdbValue value = new RdbValue();
            value.setUmiType(UmiTypes.View);
            value.setValue(rs.getString("name"));
            result.add(value);
        }
        result.sort((o1, o2) -> ((RdbValue) o1).getValue().compareTo(((RdbValue) o2).getValue()));
        return result;
    }
}
