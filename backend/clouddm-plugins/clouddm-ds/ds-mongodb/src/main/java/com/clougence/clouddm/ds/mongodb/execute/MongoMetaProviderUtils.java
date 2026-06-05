/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
