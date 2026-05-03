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

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.clougence.utils.HexadecimalUtils;

/**
 * PostgreSQL，数组类型
 * @version : 2023-02-19
 * @author 赵永春 (zyc@hasor.net)
 */
public class PgArrayTypeHandler extends ArrayTypeHandler {

    protected final String                   elementType;
    protected final PostgresReadArrayHandler readArrayHandler;

    public PgArrayTypeHandler(String elementType, int dimCount){
        this.elementType = elementType;
        this.readArrayHandler = createPostgresReadArrayHandler(elementType);
    }

    public PgArrayTypeHandler(String elementType, PostgresReadArrayHandler readArrayHandler){
        this.elementType = elementType;
        this.readArrayHandler = readArrayHandler;
    }

    protected PostgresReadArrayHandler createPostgresReadArrayHandler(String elementType) {
        switch (elementType) {
            case "money":
                return rs -> PgMoneyAsBigDecimalTypeHandler.toNumber(rs.getString("VALUE"));
            case "bit":
            case "varbit":
            case "geometry":
                return rs -> rs.getString("VALUE");
            case "bytea":
                return rs -> rs.getBytes("VALUE");
            default:
                return rs -> rs.getObject("VALUE");
        }
    }

    protected Object[] objects(Object parameter) {
        Object[] oriData = (Object[]) parameter;

        List<Object> copy = new ArrayList<>();
        for (Object oriDatum : oriData) {
            if (this.elementType.equals("bytea")) {
                copy.add(HexadecimalUtils.bytes2hex((byte[]) oriDatum));
            } else {
                copy.add(oriDatum);
            }
        }
        return copy.toArray();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (parameter instanceof Array) {
            ps.setArray(i, (Array) parameter);// it's the user's responsibility to properly free() the Array instance
        } else {
            Array array = null;
            try {
                array = ps.getConnection().createArrayOf(this.elementType, objects(parameter));
                ps.setArray(i, array);
            } finally {
                if (array != null) {
                    array.free();
                }
            }
        }
    }

    protected Object extractArray(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        List<Object> data = new ArrayList<>();
        try (ResultSet rs = array.getResultSet()) {
            while (rs.next()) {
                if (this.readArrayHandler == null) {
                    data.add(rs.getObject("VALUE"));
                } else {
                    data.add(this.readArrayHandler.readElement(rs));
                }
            }
            array.free();
            return data.toArray();
        }
    }

    public static interface PostgresReadArrayHandler {

        Object readElement(ResultSet rs) throws SQLException;
    }
}
