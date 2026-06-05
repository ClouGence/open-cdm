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
package com.clougence.clouddm.ds.polardb.execute.porx;

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
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-01-22
 */
@Slf4j
public class PorXMetaProviderDm extends MyMetaProviderDm implements MetaDataService {

    public PorXMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new PorXMetaProviderUtils();
    }

    @Override
    public List<Value> selectViews(String schema) throws SQLException {
        String sql = "select TABLE_NAME,'VIEW' as TABLE_TYPE ,COLLATION_CONNECTION as TABLE_COLLATION,null as TABLE_COMMENT, null as ENGINE from INFORMATION_SCHEMA.VIEWS " //
                     + "where TABLE_SCHEMA = ? ";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertTableName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }
}
