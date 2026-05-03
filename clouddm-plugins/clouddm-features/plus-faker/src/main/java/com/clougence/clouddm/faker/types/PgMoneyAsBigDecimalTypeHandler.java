/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.clouddm.faker.types;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BigDecimalTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.clougence.utils.StringUtils;

/**
 * PostgreSQL,Money 类型 BigDecimal 形式读写
 * @version : 2023-02-19
 * @author 赵永春 (zyc@hasor.net)
 */
public class PgMoneyAsBigDecimalTypeHandler extends BigDecimalTypeHandler {

    @Override
    public BigDecimal getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toNumber(rs.getString(columnName));
    }

    @Override
    public BigDecimal getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toNumber(rs.getString(columnIndex));
    }

    @Override
    public BigDecimal getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toNumber(cs.getString(columnIndex));
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BigDecimal parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toPlainString());
    }

    protected static BigDecimal toNumber(String moneyValue) {
        String moneySign = filerMoneySign(moneyValue);
        return StringUtils.isBlank(moneySign) ? null : new BigDecimal(moneySign);
    }

    protected static String filerMoneySign(String mStr) {
        if (StringUtils.isBlank(mStr)) {
            return null;
        }
        char[] chars = mStr.toCharArray();
        int index = -1;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isDigit(c)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return StringUtils.replace(mStr, ",", "");
        } else {
            return StringUtils.replace(mStr.substring(index), ",", "");
        }
    }
}
