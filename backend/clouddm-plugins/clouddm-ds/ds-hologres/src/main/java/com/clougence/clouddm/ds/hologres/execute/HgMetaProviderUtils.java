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
package com.clougence.clouddm.ds.hologres.execute;

import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.adapter.hologres.HgSqlTypes;
import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaProviderUtils;
import com.clougence.schema.metadata.FieldType;

import lombok.extern.slf4j.Slf4j;

/**
 * Postgres 元信息获取，参考资料：
 * <li>https://www.postgresql.org/docs/13/information-schema.html</li>
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class HgMetaProviderUtils extends PgMetaProviderUtils {

    @Override
    protected FieldType safeToPostgresTypes(long serverVersionNumber, ResultSet rs) throws SQLException {
        if (rs == null) {
            return null;
        }
        Long typeOid = tryWasNull(rs.getLong("type_oid"), rs);
        String defaultValue = rs.getString("column_default");
        String typeName = rs.getString("type_name");

        HgSqlTypes pgTypeEnum = HgSqlTypes.valueOfTypeOid(typeOid);
        if (pgTypeEnum == null) {
            pgTypeEnum = HgSqlTypes.valueOfCode(typeName);
        }

        //        if (defaultValue != null && defaultValue.contains("nextval(")) {
        //            boolean gte_9_2 = serverVersionNumber >= PgServerVersion.v9_2.getVersionNum();
        //            if (pgTypeEnum == PolarDBPgTypes.INTEGER) {
        //                pgTypeEnum = PolarDBPgTypes.SERIAL;
        //            } else if (pgTypeEnum == PolarDBPgTypes.BIGINT) {
        //                pgTypeEnum = PolarDBPgTypes.BIGSERIAL;
        //            } else if (pgTypeEnum == PolarDBPgTypes.SMALLINT && gte_9_2) {
        //                pgTypeEnum = PolarDBPgTypes.SMALLSERIAL;
        //            }
        //        }

        return pgTypeEnum;
    }

}
