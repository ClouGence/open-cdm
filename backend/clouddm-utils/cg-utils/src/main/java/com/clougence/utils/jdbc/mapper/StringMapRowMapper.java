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
package com.clougence.utils.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.utils.jdbc.RowMapper;

public class StringMapRowMapper implements RowMapper<Map<String, String>> {

    private final Map<String, Integer> extractColumn;

    public StringMapRowMapper(Map<String, Integer> extractColumn){
        this.extractColumn = extractColumn;
    }

    @Override
    public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, String> resultData = new LinkedHashMap<>();
        for (String columnName : extractColumn.keySet()) {
            resultData.put(columnName, rs.getString(columnName));
        }
        return resultData;
    }
}
