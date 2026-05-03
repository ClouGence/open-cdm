package com.clougence.clouddm.sec.rules.domain.func;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintsDTO;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintService;
import com.clougence.detectrule.lang.reflect.RuleFunction;
import com.clougence.utils.StringUtils;

public class FuncConstraintUtils {

    public static FuncConstraintUtils INSTANCE = new FuncConstraintUtils();

    private QueryConstraintService    constraintService;

    public void setConstraintService(QueryConstraintService queryConstraintService) { this.constraintService = queryConstraintService; }

    @RuleFunction("checkSelectColumn")
    public List<String> checkSelectColumn(String primaryUid, long dsId, List<SelectItem> items) {
        List<RealColumn> columnList = new ArrayList<>();
        for (SelectItem column : items) {
            columnList.addAll(column.getColumns());
        }

        List<String> collect = columnList.stream().map(column -> {
            StringBuilder sb = new StringBuilder();
            if (column.getCatalog() != null) {
                sb.append("/").append(column.getCatalog());
            }
            sb.append("/").append(column.getSchema());
            sb.append("/").append(column.getTable());
            return sb.toString();
        }).distinct().collect(Collectors.toList());

        // select 1, no column
        if (collect.isEmpty()) {
            return successResult();
        }
        List<QueryConstraintsDTO> dtos = constraintService.fetchQueryConstraints(primaryUid, dsId, collect);
        if (dtos.isEmpty()) {
            return successResult();
        }

        Map<String, Map<Integer, List<RealColumn>>> map = new HashMap<>();
        for (RealColumn column : columnList) {
            String key = getKey(column);
            Map<Integer, List<RealColumn>> tableIdMap = map.computeIfAbsent(key, (k) -> new HashMap<>());
            List<RealColumn> realColumns = tableIdMap.computeIfAbsent(column.getTableId(), (k) -> new ArrayList<>());
            realColumns.add(column);
        }

        StringBuilder sb = new StringBuilder();
        for (QueryConstraintsDTO column : dtos) {
            check(column, map, sb);
        }

        String columns = sb.toString();
        if (StringUtils.isNotEmpty(columns)) {
            String message = columns.substring(0, columns.length() - 2);
            FuncOutParamUtils.getOutParams().put("columns", message);

            List<String> result = new ArrayList<>();
            result.add(message);
            result.add("false");
            return result;
        }

        return successResult();
    }

    private List<String> successResult() {
        List<String> result = new ArrayList<>();
        result.add("");
        result.add("true");

        return result;
    }

    private String getKey(RealColumn column) {
        StringBuilder sb = new StringBuilder("/");
        if (column.getCatalog() != null) {
            sb.append(column.getCatalog()).append("/");
        }

        sb.append(column.getSchema()).append("/").append(column.getTable());
        return sb.toString();
    }

    private void check(QueryConstraintsDTO dto, Map<String, Map<Integer, List<RealColumn>>> map, StringBuilder sb) {
        String key = dto.getPath();

        Map<Integer, List<RealColumn>> tableIdMap = map.get(key);
        if (tableIdMap == null) {
            return;
        }

        Set<String> needSelectColumns = dto.getConstraints().stream().map(QueryConstraintsDTO.Constraint::getColumn).collect(Collectors.toSet());

        for (Integer tableId : tableIdMap.keySet()) {
            List<RealColumn> realColumns = tableIdMap.get(tableId);
            Set<String> selectColumns = realColumns.stream().filter(RealColumn::isOnlyOneColumn).map(RealColumn::getColumn).collect(Collectors.toSet());

            Set<String> withoutColumns = new HashSet<>(needSelectColumns);
            withoutColumns.removeAll(selectColumns);

            if (!withoutColumns.isEmpty()) {
                sb.append("{TABLE: ").append(dto.getPath()).append(", COLUMNS:[");

                for (String column : withoutColumns) {
                    sb.append(column);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append("]} ,");
            }
        }
    }

}
