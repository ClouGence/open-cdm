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
package com.clougence.clouddm.ds.goldendb.execute.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.dsfamily.mysql.execute.MyUmiServiceDm;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;

public class GoldenDBMySQLUmiServiceDm extends MyUmiServiceDm {

    private static final Set<String> UNAVAILABLE_SCHEMAS = Set.of("_gdb_audit", "dbagent", "information_schema", "performance_schema", "recyclebin");

    public GoldenDBMySQLUmiServiceDm(Connection connection){
        super(new GoldenDBMySQLMetaProvider(connection));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        List<Value> values = super.listLevels(levels, levelsParam);
        return values.stream().filter(value -> {
            String schema = StringUtils.trimToEmpty(value.asValue()).toLowerCase(Locale.ROOT);
            return !UNAVAILABLE_SCHEMAS.contains(schema);
        }).toList();
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        if (leafType == UmiTypes.Table || leafType == UmiTypes.View) {
            return super.listLeaf(levelsParam, leafType, pattern);
        }
        throw new UnsupportedOperationException("listLeaf of " + leafType + " Unsupported.");
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        if (leafType == UmiTypes.Catalog || leafType == UmiTypes.Schema || leafType == UmiTypes.Table || leafType == UmiTypes.View) {
            return super.detailLeaf(levelsParam, leafType, leafName);
        }
        throw new UnsupportedOperationException("detailLeaf of " + leafType + " Unsupported.");
    }
}
