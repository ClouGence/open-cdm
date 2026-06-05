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
package com.clougence.clouddm.ds.tidb.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.struts.Value;

import lombok.extern.slf4j.Slf4j;

/**
 * TiDB 元信息获取，参考资料：
 * <li>https://dev.TiDB.com/doc/refman/8.0/en/information-schema.html</li>
 *
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class TiMetaProviderDm extends MyMetaProviderDm implements MetaDataService {

    public TiMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new TiMetaProviderUtils();
    }

    public List<Value> selectSequences(String schema) throws SQLException {
        String sql = "select * from INFORMATION_SCHEMA.SEQUENCES where SEQUENCE_SCHEMA = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = TiMetaProviderUtils.convertSequences(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }
}
