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
