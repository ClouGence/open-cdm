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
package com.clougence.clouddm.ds.kingbasees.execute.sqlserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.dsfamily.sqlserver.execute.MsSqlUmiServiceDm;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;

public class KingbaseESSQLServerUmiServiceDm extends MsSqlUmiServiceDm {

    private static final Set<String> SYSTEM_CATALOGS = Set.of("kingbase", "security", "template0", "template1");
    private static final Set<String> SYSTEM_SCHEMAS  = Set
        .of("anon", "dbms_job", "dbms_scheduler", "dbms_sql", "information_schema", "kdb_schedule", "perf", "pg_bitmapindex", "pg_catalog", "src_restrict", "sys", "sys_catalog", "sys_hm", "sysaudit", "sysmac", "wmsys", "xlog_record_read");

    public KingbaseESSQLServerUmiServiceDm(Connection connection){
        super(new KingbaseESSQLServerMetaProviderDm(connection));
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        List<Value> values = super.listLevels(levels, levelsParam);
        if (levels.isEmpty()) {
            return values.stream().filter(value -> {
                String catalog = StringUtils.trimToEmpty(value.asValue()).toLowerCase(Locale.ROOT);
                return !SYSTEM_CATALOGS.contains(catalog);
            }).toList();
        }
        if (levels.size() != 1) {
            return values;
        }
        return values.stream().filter(value -> {
            String schema = StringUtils.trimToEmpty(value.asValue()).toLowerCase(Locale.ROOT);
            return !SYSTEM_SCHEMAS.contains(schema) && !schema.startsWith("pg_temp_") && !schema.startsWith("pg_toast");
        }).toList();
    }
}
